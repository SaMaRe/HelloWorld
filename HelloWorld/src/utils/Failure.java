package utils;

public class Failure<T>  implements Either{
	
	private Throwable err;

	@Override
	public boolean isSuccess() {
		
		return false;
	}

	@Override
	public boolean isFailure() {
		
		return true;
	}

	@Override
	public Throwable getError() {
		return err;
	}

	@Override
	public Object getObject() {
		return null;
	}
	

}
