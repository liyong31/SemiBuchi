package complement.nwa;

public class DoubleDecker {
	
	private int mDownState;
	private int mUpState;
		
	public DoubleDecker(int down, int up) {
		this.mDownState = down;
		this.mUpState = up;
	}
	
	public int getDownState() {
		return mDownState;
	}
	
	public int getUpState() {
		return mUpState;
	}
	
	@Override
	public boolean equals(Object other) {
		if(this == other) return true;
		if(! (other instanceof DoubleDecker)) {
			return false;
		}
		DoubleDecker otherDecker = (DoubleDecker)other;
		return this.mDownState == otherDecker.mDownState
			&& this.mUpState == otherDecker.mUpState;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
        int result = 1;
        result = prime * result + this.mDownState;
        result = prime * result + this.mUpState;
        return result;
	}
	
	@Override
	public String toString() {
		return "<down:" + this.mDownState + ", up:" +this.mUpState + ">"; 
	}
	
	public static final int EMPTY_DOWN_STATE = -1;

}
