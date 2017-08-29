package util.parser.nwa.ats;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import automata.BuchiNwa;
import automata.IBuchiNwa;
import complement.BuchiNwaComplementSDBA;
import complement.DoubleDecker;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import main.Options;
import util.IntSet;
import util.UtilIntSet;


public class ATSFileParser4Nwa {
	
	private List<String> mAlphabets;
	private List<IBuchiNwa> mBuchiNwaList;
	
	private Map<String, Integer> mAlphabetMap;
	private Map<String, Integer> mStateMap;
	
	private IntSet mCallAlphabet;
	private IntSet mInternalAlphabet;
	private IntSet mReturnAlphabet;
	
	
	public ATSFileParser4Nwa() {
		this.mAlphabets = new ArrayList<>();
		this.mBuchiNwaList = new ArrayList<>();
		this.mAlphabetMap = new HashMap<>();
		this.mStateMap = new HashMap<>();
		this.mCallAlphabet = UtilIntSet.newIntSet();
		this.mInternalAlphabet = UtilIntSet.newIntSet();
		this.mReturnAlphabet = UtilIntSet.newIntSet();
	}
	
	protected void addBuchiNwa(IBuchiNwa buchi) {
		mBuchiNwaList.add(buchi);
	}
	
	public List<String> getAlphabet() {
		return Collections.unmodifiableList(mAlphabets);
	}
	
	protected int getLetterId(String letterStr) {
		return mAlphabetMap.get(letterStr);
	}
	
	protected int getStateId(String stateStr) {
		return mStateMap.get(stateStr);
	}
	
	protected IBuchiNwa makeBuchiNwa() {
		return new BuchiNwa(mCallAlphabet, mInternalAlphabet, mReturnAlphabet);
	}
	
	private void addLetter(IntSet alphabet, String letterStr) {
		if(! mAlphabetMap.containsKey(letterStr)) {
			int id = mAlphabets.size();
			mAlphabetMap.put(letterStr, id);
			mAlphabets.add(letterStr);
			alphabet.set(id);
		}
	}
	
	protected void addLetterCall(String letterStr) {
		addLetter(mCallAlphabet, letterStr);
	}
	
	protected void addLetterInternal(String letterStr) {
		addLetter(mInternalAlphabet, letterStr);
	}
	
	protected void addLetterReturn(String letterStr) {
		addLetter(mReturnAlphabet, letterStr);
	}
	
	public int getAlphabetSize() {
		return mAlphabets.size();
	}
	
	// not necessary
	public void clear() {
		mAlphabetMap.clear();
		mStateMap.clear();
//		mAlphabets.clear();
		mCallAlphabet.clear();
		mInternalAlphabet.clear();
		mReturnAlphabet.clear();
	}
	
	protected void putState(String stateStr, int state) {
		mStateMap.put(stateStr, state);
	}
	
	public List<IBuchiNwa> getBuchiNwaList() {
		return Collections.unmodifiableList(mBuchiNwaList);
	}
	
	public void parse(String file) {
		try {
			FileInputStream inputStream = new FileInputStream(new File(file));
			ATSNwaParser parser = new ATSNwaParser(inputStream);
			parser.parse(this);
			clear();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public IBuchiNwa getBuchi(int index) {
		assert index < mBuchiNwaList.size();
		return mBuchiNwaList.get(index);
	}
	
	
	public static void main(String[] args) {
		
//		TObjectIntMap<DoubleDecker> map = new TObjectIntHashMap<>();
//		map.put(new DoubleDecker(-1, 2), 0);
//		System.out.println(map.containsKey(new DoubleDecker(-1, 2)));
		ATSFileParser4Nwa parser = new ATSFileParser4Nwa();
		parser.parse("/home/liyong/workspace-neon/SemiBuchi/test.ats");
		IBuchiNwa buchi = parser.getBuchi(0);
		buchi.toATS(System.out, parser.getAlphabet());
		Options.setChoice = 3;
		BuchiNwaComplementSDBA complement = new BuchiNwaComplementSDBA(buchi);
		Options.verbose = false;
		Options.optNCSB = true;
		complement.explore();
		System.out.println("#states: " + complement.getStateSize() + ", #trans: " + complement.getNumTransition());
		complement.toATS(System.out, parser.getAlphabet());
	}
	
	

}
