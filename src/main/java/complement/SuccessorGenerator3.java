package complement;

import main.Options;
import util.IntSet;
import util.PowerSet;

class SuccessorGenerator3 {
	
	private boolean mIsCurrBEmpty;
	private final NCSB mSuccNCSB;
	
	private IntSet mMinusFSuccs;
	private IntSet mInterFSuccs;
	
	private IntSet mF;       // so far all final states
	
	private IntSet mNPrime;  // d(N)\F\B'\S'
	private IntSet mVPrime;  // d(C) \/ (d(N) /\ F)
	private IntSet mSPrime;  // d(S)
	private IntSet mBPrime;  // d(B)
	
//	private IntSet mNInterFSuccs;
	
	private PowerSet mPs;
	
	private boolean hasSuccessors = true;
	
		
	public SuccessorGenerator3(boolean isCurrBEmpty, NCSB succ, IntSet minusFSuccs, IntSet interFSuccs, IntSet f) {
		this.mIsCurrBEmpty = isCurrBEmpty;
		this.mSuccNCSB = succ;
		
		this.mMinusFSuccs = minusFSuccs;
		this.mInterFSuccs = interFSuccs;
		this.mF = f;
		
		// initialization
		// N'
		mNPrime =  this.mSuccNCSB.copyNSet();
		mNPrime.andNot(mF);                    // remove final states
		mNPrime.andNot(mSuccNCSB.getCSet());   // remove successors of C, the final states of NSuccs are in CSuccs 
		mNPrime.andNot(mSuccNCSB.getSSet());   // remove successors of S
		
		// V' = d(C) \/ (d(N)/\F)
		mVPrime =  mSuccNCSB.copyCSet();
		IntSet nInterFSuccs =  mSuccNCSB.copyNSet();
		nInterFSuccs.and(mF);           // (d(N) /\ F)
		mVPrime.or(nInterFSuccs);       // d(C) \/ (d(N) /\ F)
		
		// S successors
		mSPrime =  mSuccNCSB.copySSet();
		
		// B successors
		mBPrime =  mSuccNCSB.copyBSet();
		
		if(Options.optNCSB && mIsCurrBEmpty) {
			mInterFSuccs = mSuccNCSB.copyCSet(); // set to d(C)
		}
		
		// Original NCSB, distribute states in mInterFSuccs to S'
		mInterFSuccs.andNot(mMinusFSuccs);     // remove must-in C (B) states
//		mInterFSuccs.andNot(mSPrime);          // remove must in S states
		mInterFSuccs.andNot(mF);               // remove final states 

		mPs = new PowerSet(mInterFSuccs);
		
		// d(C\F) /\ d(S) or d(B/\F) should be empty
		hasSuccessors = !mMinusFSuccs.overlap(mSPrime);
	}
	
	public boolean hasNext() {
		return hasSuccessors && mPs.hasNext();
	}
	
	public NCSB next() {
		IntSet Sextra = mPs.next(); // extra states to be added into S'
		
		// this is implementation for NCSB 
		IntSet NP = mNPrime;
		IntSet CP =  null;
		IntSet SP =  mSPrime.clone();
		IntSet BP = null;
		
		if(Options.optNCSB) {
			if(mIsCurrBEmpty) {
				// as usual S and C
				CP = mVPrime.clone();
				CP.andNot(Sextra); // C' get extra
				if(Options.optBeqC) {
					BP = CP;
				}else {
					// following is d(C) /\ C'
					BP = mSuccNCSB.copyCSet(); 
					BP.andNot(Sextra);   // B'= d(C) /\ C'
				}
				SP.or(Sextra); // S'=d(S)\/(V'\C')
			}else {
				// B is not empty
				SP.or(Sextra); // d(S) \/ M'
				BP = mBPrime.clone();
				BP.andNot(Sextra); // B'=d(B)\M'
				CP = mVPrime.clone();
				CP.andNot(SP); // C'= V'\S'
			}
			
			if(SP.overlap(mF) || BP.overlap(SP)) {
				return null;
			}

		}else {
			// original NCSB
			CP = mVPrime.clone();
			CP.andNot(Sextra);
			SP.or(Sextra);
			if(mIsCurrBEmpty) {
				BP =  CP;
			}else {
				BP =  mBPrime.clone();
				BP.andNot(Sextra);
			}
			
			if(SP.overlap(mF) || CP.overlap(SP)) {
				return null;
			}
		}

		return new NCSB(NP, CP, SP, BP);
	}
	
	
	

}
