package corelibrary.model;

public class InferenceResult
{
	private boolean IsSucceed;
	public final boolean getIsSucceed()
	{
		return IsSucceed;
	}
	public final void setIsSucceed(boolean value)
	{
		IsSucceed = value;
	}
	private ModelOutput Model;
	public final ModelOutput getModel()
	{
		return Model;
	}
	public final void setModel(ModelOutput value)
	{
		Model = value;
	}
}