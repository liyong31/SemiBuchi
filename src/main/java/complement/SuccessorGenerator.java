package complement;

import util.IntSet;
import util.PowerSet;

public class SuccessorGenerator {
	
	private final NCSB mCurrNCSB;
	private final NCSB mSuccNCSB;
	
	private IntSet mCMinusFSuccs;
	private IntSet mCInterFSuccs;
	
	private IntSet mF;       // so far all final states
	
	private IntSet mNPrime;  // d(N)\F\B'\S'
	private IntSet mVPrime;  // d(C) \/ (d(N) /\ F)
	private IntSet mSPrime;  // d(S)
	private IntSet mBPrime;  // d(B)
	
	private PowerSet mPs;
	
	private boolean hasSuccessors;
	
		
	public SuccessorGenerator(NCSB curr, NCSB succ, IntSet cMinusFSuccs, IntSet cInterFSuccs, IntSet f) {
		this.mCurrNCSB = curr;
		this.mSuccNCSB = succ;
		
		this.mCMinusFSuccs = cMinusFSuccs;
		this.mCInterFSuccs = cInterFSuccs;
		this.mF = f;
		
		// initialization
		// N
		mNPrime =  this.mSuccNCSB.getNSet().clone();
		
		mNPrime.andNot(mF);                    // remove final states
		mNPrime.andNot(mSuccNCSB.getCSet());   // remove successors of C, the final states of NSuccs are in CSuccs 
		mNPrime.andNot(mSuccNCSB.getSSet());   // remove successors of S
		
		// V
		mVPrime =  mSuccNCSB.getCSet().clone();
		IntSet nInterF =  mSuccNCSB.getNSet().clone();
		nInterF.and(mF);
		mVPrime.or(nInterF);       // d(C) \/ (d(N) /\ F)
		
		// S successors
		mSPrime =  mSuccNCSB.getSSet();
		
		// B successors
		mBPrime =  mSuccNCSB.getBSet();
		
		// -------------- following is the version for NCSB
		
		mCInterFSuccs.andNot(mCMinusFSuccs);         // remove must-in C states
		mCInterFSuccs.andNot(mSPrime);               // remove must in S states
		mCInterFSuccs.andNot(mF);               // remove final states 
		// the successors of C /\ F should go to C and S with nondeterministic choices
		mPs = new PowerSet(mCInterFSuccs);
		
		// d(C\F) /\ d(S) should be empty
		hasSuccessors = !mCMinusFSuccs.overlap(mSPrime);
	}
	
	public boolean hasNext() {
		return hasSuccessors && mPs.hasNext();
	}
	
	public NCSB next() {
		IntSet Sextra = mPs.next(); // extra states to be added into S'
		
		// this is implementation for NCSB 
		IntSet NP = mNPrime;
		IntSet CP =  mVPrime.clone();
		CP.andNot(Sextra);
		IntSet SP =  mSPrime.clone();
		SP.or(Sextra);
		IntSet BP = null;
		if(mCurrNCSB.getBSet().isEmpty()) {
			BP =  CP;
		}else {
			BP =  mBPrime.clone();
			BP.andNot(Sextra);
		}

		assert ! SP.overlap(mF) && !CP.overlap(SP);
		return new NCSB(NP, CP, SP, BP);
	}
	
	
	

}
