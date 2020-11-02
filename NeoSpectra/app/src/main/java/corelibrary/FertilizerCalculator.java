package corelibrary;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import corelibrary.helpers.FileHelpers;
import corelibrary.model.DataMappingNPK;
import corelibrary.model.FertilizerData;
import corelibrary.model.FertilizerInfo;

public class FertilizerCalculator
{
	private boolean IsReady = false;
	private String DataPath;
	public final String getDataPath()
	{
		return DataPath;
	}
	public final void setDataPath(String value)
	{
		DataPath = value;
	}
	private ArrayList<FertilizerData> Datas;
	public final ArrayList<FertilizerData> getDatas()
	{
		return Datas;
	}
	public final void setDatas(ArrayList<FertilizerData> value)
	{
		Datas = value;
	}
	private ArrayList<DataMappingNPK> DataNPK;
	public final ArrayList<DataMappingNPK> getDataNPK()
	{
		return DataNPK;
	}
	public final void setDataNPK(ArrayList<DataMappingNPK> value)
	{
		DataNPK = value;
	}
	Gson gson = new Gson();
	public FertilizerCalculator()
	{
		if (getDatas() == null) {
			String json = Resources.GetResources("Data");

			FertilizerData[] datas = gson.fromJson(json, FertilizerData[].class);
			ArrayList<FertilizerData> list = new ArrayList<FertilizerData>(Arrays.asList(datas));
			setDatas(list);
			IsReady = true;
		}
		if (getDataNPK() == null)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			String[] RowData = Resources.GetResources("NPK").split(System.lineSeparator());
			int RowCounter = 0;
			setDataNPK(new ArrayList<DataMappingNPK>());
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			for (String row : RowData)
			{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:is
				String[] cols = row.split("[,]", -1);
				RowCounter++;
				if (RowCounter > 1 && cols.length == 6)
				{
					DataMappingNPK tempVar = new DataMappingNPK();
					tempVar.setNo(Integer.parseInt(cols[0]));
					tempVar.setP205(cols[1]);
					tempVar.setK2O(cols[2]);
					tempVar.setNPK(Float.parseFloat(cols[3]));
					tempVar.setUrea(Float.parseFloat(cols[4]));
					tempVar.setJenis(cols[5]);
					getDataNPK().add(tempVar);
				}
			}

		}
	}

	public FertilizerCalculator(String PathToData)
	{
		try
		{
			if ((new File(PathToData)).isFile())
			{
				this.setDataPath(PathToData);
				String json = FileHelpers.readFile(PathToData);
				FertilizerData[] datas = gson.fromJson(json, FertilizerData[].class);
				ArrayList<FertilizerData> list = new ArrayList<FertilizerData>(Arrays.asList(datas));
				setDatas(list);

				IsReady = true;
			}
			if (getDataNPK() == null)
			{
				String[] RowData = Resources.GetResources("NPK").split(System.lineSeparator());
				int RowCounter = 0;
				setDataNPK(new ArrayList<DataMappingNPK>());
				for (String row : RowData)
				{
					String[] cols = row.split("[,]", -1);
					RowCounter++;
					if (RowCounter > 1 && cols.length == 6)
					{
						DataMappingNPK tempVar = new DataMappingNPK();
						tempVar.setNo(Integer.parseInt(cols[0]));
						tempVar.setP205(cols[1]);
						tempVar.setK2O(cols[2]);
						tempVar.setNPK(Float.parseFloat(cols[3]));
						tempVar.setUrea(Float.parseFloat(cols[4]));
						tempVar.setJenis(cols[5]);
						getDataNPK().add(tempVar);
					}
				}

			}
		}
		catch (Exception e)
		{
			IsReady = false;
		}
	}


	public final double GetFertilizerDoze(double Unsur, String Tanaman)
	{
		return GetFertilizerDoze(Unsur, Tanaman, "Urea");
	}

	public final double GetFertilizerDoze(double Unsur)
	{
		return GetFertilizerDoze(Unsur, "Padi", "Urea");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public double GetFertilizerDoze(double Unsur, string Tanaman = "Padi", string Pupuk = "Urea")

	public final double GetFertilizerDoze(double Unsur, String Tanaman, String Pupuk)
	{
		if (!IsReady)
		{
			throw new RuntimeException("Recommendation Data is not found.");
		}
		/*
		var selConstant = from x in Datas
		                  where x.Pupuk == Pupuk && x.Tanaman == Tanaman
		                  select x;*/

		ArrayList<FertilizerData> selConstant = new ArrayList<FertilizerData>();
		for (FertilizerData item : getDatas())
		{
			if (item.getPupuk().equals(Pupuk) && item.getTanaman().equals(Tanaman))
			{
				selConstant.add(item);
			}
		}
		if (selConstant != null && selConstant.size() > 0)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			FertilizerData Node = selConstant.get(0);

			return (1 - Node.getC1() * Unsur) / Node.getC2();
			//if (Pupuk == "Urea")
			//{
			//    return ((1 - Node.C1 * Unsur) / Node.C2) * 100 /45;
			//}
			//else if (Pupuk == "SP36")
			//{
			//    return ((1 - Node.C1 * Unsur) / Node.C2) * 100 / 36;
			//}
			//else if (Pupuk == "KCL")
			//{
			//    return ((1 - Node.C1 * Unsur) / Node.C2) * 100 / 60;
			//}
		}
		return -1;
	}


	public final FertilizerInfo GetNPKDoze(float P2O5, float K2O)
	{
		return GetNPKDoze(P2O5, K2O, "Padi");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public FertilizerInfo GetNPKDoze(float P2O5, float K2O, string Jenis = "Padi")
	public final FertilizerInfo GetNPKDoze(float P2O5, float K2O, String Jenis)
	{
		/*
		var conditionList = from x in DataNPK
		                    where x.Jenis == Jenis
		                    select x;
		                    */
		ArrayList<DataMappingNPK> conditionList = new ArrayList<DataMappingNPK>();
		for (DataMappingNPK item : getDataNPK())
		{
			if (item.getJenis().equals(Jenis))
			{
				conditionList.add(item);
			}
		}

		for (DataMappingNPK item : conditionList)
		{
			String conditionA = item.getP205().replace("$A", String.format("%.2f", P2O5));
			String conditionB = item.getK2O().replace("$B", String.format("%.2f", K2O));
			boolean a = LogicEvaluator.EvaluateLogicalExpression(conditionA);
			boolean b = LogicEvaluator.EvaluateLogicalExpression(conditionB);
			if (a && b)
			{
				FertilizerInfo tempVar = new FertilizerInfo();
				tempVar.setNPK(item.getNPK());
				tempVar.setUrea(item.getUrea());
				return tempVar;
			}

		}
		FertilizerInfo tempVar2 = new FertilizerInfo();
		tempVar2.setUrea(0);
		tempVar2.setNPK(0);
		return tempVar2;
	}
}