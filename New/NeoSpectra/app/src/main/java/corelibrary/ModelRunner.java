package corelibrary;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import corelibrary.helpers.FileHelpers;
import corelibrary.helpers.Stopwatch;
import corelibrary.model.InferenceResult;
import corelibrary.model.ModelOutput;

public class ModelRunner
{
	@FunctionalInterface
	public interface LogDataUpdateHandler
	{
		void invoke(String LogType, String Message);
	}
	public tangible.Event<LogDataUpdateHandler> LogData = new tangible.Event<LogDataUpdateHandler>();

	private void OnLogDataUpdate(String LogType, String Message)
	{
		System.out.println(Message);
		if(! LogData.listeners().isEmpty() )
			for(LogDataUpdateHandler handler : LogData.listeners()){
				handler.invoke(LogType, Message);
			}

	}
	private static String OutputFile = "output.json";
	public static String getOutputFile()
	{
		return OutputFile;
	}
	public static void setOutputFile(String value)
	{
		OutputFile = value;
	}
	private String PathToSensorData;
	private boolean IsReady = false;
	private ArrayList<String> ListCmd;
	private String Arg;
	private String WorkingDir;
	Gson gson = new Gson();
	public ModelRunner(String WorkingDirectory, String ModelScript, String SensorData, String AnacondaFolder)
	{
		try
		{
			if (!(new File(WorkingDirectory)).isDirectory())
			{
				throw new RuntimeException("Working directory is not exists");
			}
			if (!(new File(AnacondaFolder)).isDirectory())
			{
				throw new RuntimeException("Anaconda directory is not exists");
			}
			if (!(new File(WorkingDirectory + "\\" + ModelScript)).isFile())
			{
				throw new RuntimeException("Model script is not exists");
			}


			ListCmd = new ArrayList<String>(Arrays.asList(new String[] {String.format("cd %1$s", WorkingDirectory), String.format("python %1$s %2$s", ModelScript, SensorData)}));
			Arg = String.format("/K %1$s\\Scripts\\activate.bat %2$s", AnacondaFolder, AnacondaFolder);
			WorkingDir = WorkingDirectory;
			PathToSensorData = String.format("%1$s\\%2$s", WorkingDir, SensorData);
			IsReady = true;
		}
		catch (RuntimeException ex)
		{
			IsReady = false;
			System.out.println(ex);
		}
	}


	public final InferenceResult InferenceModel(boolean DeleteInputDataAfterExecution)
	{
		try {
			return InferenceModel(DeleteInputDataAfterExecution, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  null;
	}

	public final InferenceResult InferenceModel() {
		try {
			return InferenceModel(false, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  null;
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public InferenceResult InferenceModel(bool DeleteInputDataAfterExecution = false, bool DeleteOutputAfterExecution = true)
	public final InferenceResult InferenceModel(boolean DeleteInputDataAfterExecution, boolean DeleteOutputAfterExecution) throws IOException {
		if (!IsReady)
		{
			throw new RuntimeException("Parameter must be correct.");
		}
		Stopwatch sw = new Stopwatch();
		System.out.println("start run python script...");
		//sw.Start();
		try
		{
			RunCommands(ListCmd, Arg, WorkingDir);
		}
		catch (RuntimeException ex)
		{
			System.out.println(ex);
		}
		//sw.Stop();
		System.out.println("model inference process is completed...");
		System.out.println(String.format("total time for model execution: %1$s ms", sw.elapsedTime()));
		System.out.println("try to read output...");
		String PathToOutput = String.format("%1$s\\%2$s", WorkingDir, getOutputFile());
		if ((new File(PathToOutput)).isFile())
		{
			String InferenceData = FileHelpers.readFile(PathToOutput);
			System.out.println("hasil inference model:");
			System.out.println(InferenceData);

//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:

			//ModelOutput OutputData = JsonConvert.<ModelOutput>DeserializeObject(InferenceData);
			ModelOutput OutputData = gson.fromJson(InferenceData, ModelOutput.class);
			if (DeleteOutputAfterExecution)
			{
				(new File(PathToOutput)).delete();
			}
			if (DeleteInputDataAfterExecution && (new File(PathToSensorData)).isFile())
			{
				(new File(PathToSensorData)).delete();
			}
			InferenceResult tempVar = new InferenceResult();
			tempVar.setIsSucceed(true);
			tempVar.setModel(OutputData);
			return tempVar;
		}
		else
		{
			System.out.println("output tidak ditemukan, kegagalan eksekusi model");
		}
		System.out.println("end program...");
		InferenceResult tempVar2 = new InferenceResult();
		tempVar2.setIsSucceed(false);
		tempVar2.setModel(null);
		return tempVar2;
	}

	private void RunCommands(ArrayList<String> cmds, String CmdArg)
	{
		RunCommands(cmds, CmdArg, "");
	}

	private void RunCommands(ArrayList<String> cmds)
	{
		RunCommands(cmds, "", "");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: void RunCommands(List<string> cmds, string CmdArg = "", string workingDirectory = "")
	private void RunCommands(ArrayList<String> cmds, String CmdArg, String workingDirectory)
	{
		/*
		Process process = new Process();
		ProcessStartInfo psi = new ProcessStartInfo();
		psi.FileName = "cmd.exe";
		psi.RedirectStandardInput = true;
		psi.RedirectStandardOutput = true;
		psi.CreateNoWindow = true;
		psi.RedirectStandardError = true;
		psi.UseShellExecute = false;
		psi.WorkingDirectory = workingDirectory;
		psi.Arguments = CmdArg;
		process.StartInfo = psi;
		process.Start();
		process.OutputDataReceived += (sender, e) ->
		{
				System.out.println(e.Data);
				OnLogDataUpdate("Process", e.Data);
		};
		process.ErrorDataReceived += (sender, e) ->
		{
				System.out.println(e.Data);
				OnLogDataUpdate("Error", e.Data);
		};
		process.BeginOutputReadLine();
		process.BeginErrorReadLine();
		try (OutputStreamWriter sw = process.StandardInput)
		{
			for (String cmd : cmds)
			{
				sw.write(cmd + System.lineSeparator());
			}
		}
		process.WaitForExit();*/

		//init shell
		ProcessBuilder builder = new ProcessBuilder( "cmd.exe","/K", "d:\\ProgramData\\Anaconda3\\Scripts\\activate.bat", "d:\\ProgramData\\Anaconda3" );
		if(!workingDirectory.isEmpty())
			builder.directory(new File(workingDirectory));
		Process p=null;
		try {
			p = builder.start();
		}
		catch (IOException e) {
			System.out.println(e);
		}
		//get stdin of shell
		BufferedWriter p_stdin =
				new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

		// execute the desired command (here: ls) n times
		for (String cmd : cmds) {
			try {
				//single execution
				p_stdin.write(cmd);//+ System.lineSeparator()
				p_stdin.newLine();
				p_stdin.flush();
			} catch (IOException e) {
				System.out.println(e);
			}

		}
		/*
		int n=10;
		for (int i=0; i<n; i++) {
			try {
				//single execution
				p_stdin.write("ls");
				p_stdin.newLine();
				p_stdin.flush();
			}
			catch (IOException e) {
				System.out.println(e);
			}
		}
		*/

		// finally close the shell by execution exit command
		try {
			p_stdin.write("exit");
			p_stdin.newLine();
			p_stdin.flush();
		}
		catch (IOException e) {
			System.out.println(e);
		}

		// write stdout of shell (=output of all commands)
		/*
		Scanner s = new Scanner( p.getInputStream() );
		while (s.hasNext())
		{
			OnLogDataUpdate("Process", s.next());
			//System.out.println( s.next() );
		}
		s.close();
		*/
		try {
			p.waitFor(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//String stdOut = getInputAsString(p.getInputStream());
		//String stdErr = getInputAsString(p.getErrorStream());
	}
	private String getInputAsString(InputStream is)
	{
		try(Scanner s = new Scanner(is))
		{
			return s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}
	}
}