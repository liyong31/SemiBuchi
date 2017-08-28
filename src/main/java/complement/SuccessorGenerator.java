package complement;

import main.Options;
import util.IntSet;
import util.PowerSet;

public class SuccessorGenerator {
	
	private final NCSB mCurrNCSB;
	private final NCSB mSuccNCSB;
	
	private IntSet mMinusFSuccs;
	private IntSet mInterFSuccs;
	
	private IntSet mF;       // so far all final states
	
	private IntSet mNPrime;  // d(N)\F\B'\S'
	private IntSet mVPrime;  // d(C) \/ (d(N) /\ F)
	private IntSet mSPrime;  // d(S)
	private IntSet mBPrime;  // d(B)
	
	private IntSet mNInterFSuccs;
	
	private PowerSet mPs;
	
	private boolean hasSuccessors;
	
		
	public SuccessorGenerator(NCSB curr, NCSB succ, IntSet minusFSuccs, IntSet interFSuccs, IntSet f) {
		this.mCurrNCSB = curr;
		this.mSuccNCSB = succ;
		
		this.mMinusFSuccs = minusFSuccs;
		this.mInterFSuccs = interFSuccs;
		this.mF = f;
		
		// initialization
		// N
		mNPrime =  this.mSuccNCSB.getNSet().clone();
		
		mNPrime.andNot(mF);                    // remove final states
		mNPrime.andNot(mSuccNCSB.getCSet());   // remove successors of C, the final states of NSuccs are in CSuccs 
		mNPrime.andNot(mSuccNCSB.getSSet());   // remove successors of S
		
		// V
		mVPrime =  mSuccNCSB.getCSet().clone();
		mNInterFSuccs =  mSuccNCSB.getNSet().clone();
		mNInterFSuccs.and(mF);           // (d(N) /\ F)
		mVPrime.or(mNInterFSuccs);       // d(C) \/ (d(N) /\ F)
		
		// S successors
		mSPrime =  mSuccNCSB.getSSet();
		
		// B successors
		mBPrime =  mSuccNCSB.getBSet();
		
		// -------------- following is the version for NCSB
		if(Options.optNCSB && mCurrNCSB.getBSet().isEmpty()) {
			mInterFSuccs = mSuccNCSB.getCSet().clone(); // set to d(C)
		}
		// Original NCSB
		mInterFSuccs.andNot(mMinusFSuccs);     // remove must-in C (B) states
		mInterFSuccs.andNot(mSPrime);          // remove must in S states
		mInterFSuccs.andNot(mF);               // remove final states 

		mPs = new PowerSet(mInterFSuccs);
		
		// d(C\F) /\ d(S) should be empty
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
			if(mCurrNCSB.getBSet().isEmpty()) {
				// as usual S and C
				CP = mVPrime.clone();
				CP.andNot(Sextra); // C' get extra
				BP = mSuccNCSB.copyCSet(); 
				BP.andNot(Sextra);   // B'= d(C) /\ C'
				SP.or(Sextra); // S'=d(S)\/(V'\C')
			}else {
				// B is not empty
				SP.or(Sextra); // d(S) \/ M'
				BP = mBPrime.clone();
				BP.andNot(Sextra); // B'=d(B)\M'
				CP = mVPrime.clone();
				CP.andNot(SP); // C'= V'\S'
			}
			if(SP.overlap(mF)) {
				return null;
			}

		}else {
			// original NCSB
			CP = mVPrime.clone();
			CP.andNot(Sextra);
			SP.or(Sextra);
			if(mCurrNCSB.getBSet().isEmpty()) {
				BP =  CP;
			}else {
				BP =  mBPrime.clone();
				BP.andNot(Sextra);
			}
		}

		assert !SP.overlap(mF) && !CP.overlap(SP);
		return new NCSB(NP, CP, SP, BP);
	}
	
	
	

}
