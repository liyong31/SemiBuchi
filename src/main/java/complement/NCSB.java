package complement;

import util.IntIterator;
import util.IntSet;
import util.UtilIntSet;

/**
 * NCSB tuple 
 * */
class NCSB {
	
	private IntSet mNSet;
	private IntSet mCSet;
	private IntSet mSSet;
	private IntSet mBSet;
	
	public NCSB(IntSet N, IntSet C, IntSet S, IntSet B) {
		this.mNSet = N;
		this.mCSet = C;
		this.mSSet = S;
		this.mBSet = B;
	}
	
	public NCSB() {
		this.mNSet = UtilIntSet.newIntSet();
		this.mCSet = UtilIntSet.newIntSet();
		this.mSSet = UtilIntSet.newIntSet();
		this.mBSet = UtilIntSet.newIntSet();
	}
	
	// be aware that we use the same object
	//CLONE object to make modification
	public IntSet getNSet() {
		return  mNSet;
	}
	
	public IntSet getCSet() {
		return  mCSet;
	}
	
	public IntSet getSSet() {
		return  mSSet;
	}
	
	public IntSet getBSet() {
		return  mBSet;
	}
	
	// Safe operations for (N, C, S, B)
	public IntSet copyNSet() {
		return  mNSet.clone();
	}
	
	public IntSet copyCSet() {
		return  mCSet.clone();
	}
	
	public IntSet copySSet() {
		return  mSSet.clone();
	}
	
	public IntSet copyBSet() {
		return  mBSet.clone();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof NCSB)) {
			return false;
		}
		NCSB ncsb = (NCSB)obj;
		return  contentEqual(ncsb);
	}
	
	public boolean contentEqual(NCSB ncsb) {
		if(! mNSet.equals(ncsb.mNSet)
		|| ! mCSet.equals(ncsb.mCSet)
		|| ! mSSet.equals(ncsb.mSSet)
		|| ! mBSet.equals(ncsb.mBSet)) {
			return false;
		}
		return true;
	}
	
	@Override
	public NCSB clone() {
		return new NCSB(mNSet.clone(), mCSet.clone(), mSSet.clone(), mBSet.clone());
	}
	
	@Override
	public String toString() {
		return "(" + mNSet.toString() + "," 
		           + mCSet.toString() + ","
		           + mSSet.toString() + ","
		           + mBSet.toString() + ")";
	}
	
    private int hashCode;
    private boolean hasCode = false;
	
	@Override
	public int hashCode() {
		if(hasCode) return hashCode;
		else {
			hasCode = true;
			hashCode = 1;
			final int prime = 31;
			hashCode= prime * hashCode + hashValue(mNSet);
			hashCode= prime * hashCode + hashValue(mCSet);
			hashCode= prime * hashCode + hashValue(mSSet);
			hashCode= prime * hashCode + hashValue(mBSet);
			return hashCode;
		}
	}
	
	private int hashValue(IntSet set) {
		final int prime = 31;
        int result = 1;
        IntIterator iter = set.iterator();
        while(iter.hasNext()) {
        	result = prime * result + iter.next();
        }
        return result;
	}
	

}
