package operation.exploration;

import java.util.Iterator;
import java.util.LinkedList;

import automata.IBuchiNwa;
import automata.IStateNwa;
import complement.BuchiNwaComplement;
import complement.DoubleDecker;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import main.Options;

import util.parser.nwa.ats.ATSFileParser4Nwa;

/**
 * Explore state space of Buchi Nested Word Automata
 * */
public final class BuchiNwaExploration extends BuchiExploration<IBuchiNwa>{

	// store those state to be explored, every state will be only added once
	private final StateInfoQueue mWorkList;
	// store those state whose down states have been updated
	private final StateInfoQueue mPropagationList;
	private final TIntObjectMap<StateInfo> mStateInfoMap;
	// (q, {q'}) for every pair q and q', there exists a run from q to q'
	// over some well-matched nested word, actually they are from return transition
	// p * q * >r -> q' during the exploration
	private final TIntObjectMap<TIntSet> mSummaryMap;
	
	private int numTrans = 0;
	
	public BuchiNwaExploration(IBuchiNwa operand) {
		super(operand);
		mWorkList = new StateInfoQueue();
		mPropagationList = new StateInfoQueue();
		mStateInfoMap = new TIntObjectHashMap<>();
		mSummaryMap = new TIntObjectHashMap<>();
	}
	
	/**
	 * When a new state was created, it will be added into mWorkList only once.
	 * when the down states of a state which has been traversed has changed, that state
	 * should be added into mPropagationList 
	 *  */
	@Override
	protected void explore() {
		
		if(mExplored) return ;
		mExplored = true;
		// initialize
		addInitialStates();
		// loop begins
		numTrans = 0;
		do{
			
			while(! mWorkList.isEmpty()) {
				/** self loop new down states will be handled separately */
				final StateInfo currInfo = mWorkList.removeFirst();
				currInfo.clearUnpropagateDownStates();
				final TIntSet newDownStatesLoops = 
						traverseReturnTransitionsWithHier(currInfo, currInfo.getDownStates());
				traverseInternalTransitions(currInfo);
				final TIntSet newDownStates = traverseCallTransitions(currInfo);
				if(newDownStates != null) {
					newDownStatesLoops.addAll(newDownStates);
				}
				
				addPropagateNewDownStates(currInfo, newDownStatesLoops);
			}
			
			/**
			 * if mWorkList is empty and propagation list is not empty, then
			 * we should propagate current new down states to successors
			 *  */
			while(mWorkList.isEmpty() && ! mPropagationList.isEmpty()) {
				propagateNewDownStates(mPropagationList.removeFirst());
			}
			
		}while(! mWorkList.isEmpty() || ! mPropagationList.isEmpty());
	}
	
	private TIntSet traverseReturnTransitionsWithHier(final StateInfo currInfo
			, final TIntSet downStates) {
		TIntSet newDownStatesLoops = new TIntHashSet();
		if(allowReturnTransitions()) {
			for(final Integer downState : iterable(downStates)){
				if(isNotEmptyDownState(downState)) {
					final TIntSet newDownStates = 
							traverseReturnTransitions(currInfo, downState);
					if(newDownStates != null) {
						newDownStatesLoops.addAll(newDownStates);
					}
				}
			}
		}
		return newDownStatesLoops;
	}
	
	
	private void addPropagateNewDownStates(final StateInfo currInfo
			, final TIntSet newDownStates) {
		assert newDownStates != null;
		if(!newDownStates.isEmpty()) {
			for(final Integer downState : iterable(newDownStates)) {
				currInfo.addDownState(downState);
			}
			addStateInfoInPropagationList(currInfo);
		}
	}
	

	private TIntSet traverseCallTransitions(final StateInfo currInfo) {
		boolean hasSelfLoops = false;
		final IStateNwa state = mOperand.getState(currInfo.getState());
		assert state != null;
		
		for(final Integer letter : mOperand.getAlphabetCall().iterable()) {
			for(final Integer succ : state.getSuccessorsCall(letter).iterable()) {
				// state * letter -> succ (downs of succ = state)
				StateInfo succInfo = getStateInfo(succ);
				final TIntSet succDownStates = new TIntHashSet();
				succDownStates.add(state.getId());
				if (succInfo == null) {
					succInfo = addState(succ, succDownStates);
				} else {
					addNewDownStates(currInfo, succInfo, succDownStates);
					if (stateInfoEqual(currInfo, succInfo)) {
						hasSelfLoops = true;
					}
				}
				numTrans ++;
			}
		}
		
		if (hasSelfLoops) {
			final TIntSet newDownStates = new TIntHashSet(1);
			newDownStates.add(state.getId());
			return addNewDownStatesToItself(currInfo, newDownStates);
		}
		return null;
	}

	private void traverseInternalTransitions(final StateInfo currInfo) {
		final IStateNwa state = mOperand.getState(currInfo.getState());
		for(final Integer letter : mOperand.getAlphabetInternal().iterable()) {
			for(final Integer succ : state.getSuccessorsInternal(letter).iterable()) {
				// state * letter -> succ (downs of succ = downs of state)
				StateInfo succInfo = getStateInfo(succ);
				if(succInfo == null) {
					succInfo = addState(succ, new TIntHashSet(currInfo.getDownStates()));
				}else {
					addNewDownStates(currInfo, succInfo, currInfo.getDownStates());
				}
				currInfo.addInternalSuccessors(succ);
				numTrans ++;
			}
		}
	}

	private TIntSet traverseReturnTransitions(final StateInfo currInfo, final int downState) {
		boolean hasSelfLoops = false;
		final IStateNwa state = mOperand.getState(currInfo.getState());
		final StateInfo downInfo = getStateInfo(downState);
		assert downInfo != null;
		
		for(final Integer letter : mOperand.getAlphabetReturn().iterable()) {
			for(final Integer succ : state.getSuccessorsReturn(downState, letter).iterable()) {
				IStateNwa succState = mOperand.getState(succ);
				assert succState != null;
				// state * downState * letter -> succState
				// (downs of succState = downs of downState)
				StateInfo succInfo = getStateInfo(succState.getId());
				if(succInfo == null) {
					succInfo = addState(succ, new TIntHashSet(downInfo.getDownStates()));
				}else {
					addNewDownStates(currInfo, succInfo, downInfo.getDownStates());
					if(stateInfoEqual(currInfo, succInfo)) {
						hasSelfLoops = true;
					}
				}
				numTrans ++;
				// record that through hierarchy state downState, succState can be reached
				// by a return transition, and if down states of downState are updated, then
				// we should also update the down states of succState since from above we know
				// that (downs of succState = downs of downState)
				addSummary(downState, succState.getId());
			}
		}
		
		if(hasSelfLoops) {
			return addNewDownStatesToItself(currInfo, downInfo.getDownStates());
		}
		return null;		
	}
	
	private void addSummary(final int hier, final int succ) {
		TIntSet succs = mSummaryMap.get(hier);
		if(succs == null) {
			succs = new TIntHashSet();
		}
		succs.add(succ);
		mSummaryMap.put(hier, succs);
	}
	
	private TIntSet getSummarySuccessors(final int hier) {
		final TIntSet result = mSummaryMap.get(hier);
		if(result == null) return new TIntHashSet();
		return result;
	}

	/**
	 * Extra addNewDownStates in order to avoid multiple same calls
	 * */
	private TIntSet addNewDownStatesToItself(final StateInfo stateInfo,
			final TIntSet downStates) {
		TIntSet newDownStates = null;
		for(final Integer down : iterable(downStates)) {
			if (!stateInfo.getDownStates().contains(down)) {
				if (newDownStates == null) {
					newDownStates = new TIntHashSet();
				}
				newDownStates.add(down);
			}
		}
		return newDownStates;
	}
	
	private boolean stateInfoEqual(final StateInfo stateInfo1
			         , final StateInfo stateInfo2) {
		return stateInfo1 == stateInfo2;
	}
	

	/**
	 * add new down states to succStateInfo with the states in downStates
	 * */
	private void addNewDownStates(final StateInfo currStateInfo, final StateInfo succStateInfo
			, final TIntSet downStates) {
		
		// will be handled at other place, avoid multiple calls for itself
		if(stateInfoEqual(currStateInfo, succStateInfo)) return ;
		
		boolean needPropagation = false;
		for(final Integer down : iterable(downStates)) {
			final boolean newlyAdded = succStateInfo.addDownState(down);
			if (newlyAdded) {
				needPropagation = true;
			}
		}
		/**
		 * If there is new down state for succStateInfo, then the down state of
		 * succStateInfo should be propagated 
		 * */
		if (needPropagation) {
			addStateInfoInPropagationList(succStateInfo);
		}
	}
	
	/** 
	 * If a state has not been traversed, then no need to be added
	 * into mPropagationList
	 * */
	private void addStateInfoInPropagationList(final StateInfo stateInfo) {
		if(! mWorkList.hasStateInfo(stateInfo)) {
			mPropagationList.add(stateInfo);
		}
	}

	private StateInfo getStateInfo(final int state) {
		return mStateInfoMap.get(state);
	}

	private boolean allowReturnTransitions() {
		return ! mOperand.getAlphabetReturn().isEmpty();
	}
	
	private boolean isNotEmptyDownState(final int state) {
		return state != DoubleDecker.EMPTY_DOWN_STATE;
	}
	
	/**
	 * If a state has been added new down states, then those down states should be propagated to
	 * 
	 *   * its internal successors, since they share the same down states
	 *   * its return successors when it is hierarchy predecessors, since they share the same down states
	 *   
	 *   Also we should add new return transitions with its new down states
	 *   */
	private void propagateNewDownStates(final StateInfo currInfo) {
		final TIntSet unpropagateDownStates = currInfo.getUnpropagateDownStates();
		if(unpropagateDownStates == null) return ;
		
		for(final Integer succ : iterable(currInfo.getInternalSuccessors())) {
			final StateInfo succInfo = getStateInfo(succ);
			addNewDownStates(currInfo, succInfo, unpropagateDownStates);
		}
		
		for(final Integer succ : iterable(getSummarySuccessors(currInfo.getState()))) {
			final StateInfo succInfo = getStateInfo(succ);
			addNewDownStates(currInfo, succInfo, unpropagateDownStates);
		}

		if(allowReturnTransitions()) {
			TIntSet newDownStatesLoops = traverseReturnTransitionsWithHier(currInfo
					                                       , unpropagateDownStates);
			currInfo.clearUnpropagateDownStates();
			// must first clear its used down states
			addPropagateNewDownStates(currInfo, newDownStatesLoops);
		}else {
			currInfo.clearUnpropagateDownStates();
		}
	}

	private void addInitialStates() {
		for(final Integer state : mOperand.getInitialStates().iterable()) {
			final TIntSet downStates = new TIntHashSet();
			addState(state, downStates);
		}
	}

	private StateInfo addState(final int state, final TIntSet downStates) {
		assert ! mStateInfoMap.containsKey(state);
		final StateInfo stateInfo = new StateInfo(state, downStates);
		mStateInfoMap.put(state, stateInfo);
		mWorkList.add(stateInfo);
		return stateInfo;
	}
	
	public int getNumTrans() {
		return numTrans;
	}
	
	private Iterable<Integer> iterable(final TIntSet set) {
		return () -> new Iterator<Integer>() {

			TIntIterator iter = set.iterator();
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public Integer next() {
				return iter.next();
			}
			
		};
	}



	//----------------------------------------------------------------
	private class StateInfo {
		
		private final int mState;
		private final TIntSet mDownStates;
		private TIntSet mUnPropagateDownStates;
		private final TIntSet mInternalSuccs; // cache the internal successors
		
		public StateInfo(final int state, final TIntSet downStates) {
			mState = state;
			mDownStates = downStates;
			mInternalSuccs = new TIntHashSet();
		}
		
		int getState() {
			return mState;
		}
		
		TIntSet getDownStates() {
			return mDownStates;
		}
		
		void clearUnpropagateDownStates() {
			mUnPropagateDownStates = null;
		}
		
		TIntSet getUnpropagateDownStates() {
			return mUnPropagateDownStates;
		}
		
		boolean addDownState(final int down) {
			if(mDownStates.contains(down)) return false;
			mDownStates.add(down);
			if (mUnPropagateDownStates == null) {
				mUnPropagateDownStates = new TIntHashSet();
			}
			mUnPropagateDownStates.add(down);
			return true;
		}
		
		void addInternalSuccessors(final int succ) {
			mInternalSuccs.add(succ);
		}
		
		TIntSet getInternalSuccessors() {
			return mInternalSuccs;
		}
		
		public String toString() {
			return "<state: " + mState + ", downs: " + mDownStates.toString() 
			     + ", unpropDowns: " + mUnPropagateDownStates + ", inSucc:" + mInternalSuccs + ">";
		}
		
	}
	
	//----------------------------------------------------------------
	private class StateInfoQueue extends LinkedList<StateInfo> {
		private static final long serialVersionUID = 1L;
		private final TIntSet mInList;
		
		public StateInfoQueue() {
			super();
			mInList = new TIntHashSet();
		}
		
		@Override
		public StateInfo removeFirst() {
			StateInfo result = super.removeFirst();
			mInList.remove(result.getState());
			return result;
		}
		
		public boolean hasStateInfo(StateInfo state) {
			return mInList.contains(state.getState());
		}
		
		@Override
		public boolean add(StateInfo state) {
			if(! mInList.contains(state.getState())) {
				mInList.add(state.getState());
				return super.add(state);
			}
			return false;
		}
		
	}
	
	//----------------------------------------------------------------
	
	public static void main(String[] args) {
		String file = "/home/liyong/workspace-neon/SemiBuchi/test4.ats";

		ATSFileParser4Nwa parser = new ATSFileParser4Nwa();
		parser.parse(file);
		IBuchiNwa buchi = parser.getBuchi(0);
		buchi.toATS(System.out, parser.getAlphabet());
		Options.setChoice = 3;
		BuchiNwaComplement complement = new BuchiNwaComplement(buchi);
		Options.verbose = false;
		Options.lazyS = false;
		Options.lazyB = true;
		
		BuchiNwaExploration reach = new BuchiNwaExploration(complement);
		reach.explore();
		System.out.println("#states: " + complement.getStateSize() + ", #trans: " + complement.getNumTransition());
		System.out.println(reach.getNumTrans());
		complement.toATS(System.out, parser.getAlphabet());
		complement.toDot(System.out, parser.getAlphabet());
		
//		System.out.println("normal exploration ----------------");
//		
		parser = new ATSFileParser4Nwa();
		parser.parse(file);
		buchi = parser.getBuchi(0);
//		buchi.toATS(System.out, parser.getAlphabet());
		complement = new BuchiNwaComplement(buchi);

		complement.explore();
		System.out.println("#states: " + complement.getStateSize() + ", #trans: " + complement.getNumTransition());
		complement.toATS(System.out, parser.getAlphabet());
	}

}
