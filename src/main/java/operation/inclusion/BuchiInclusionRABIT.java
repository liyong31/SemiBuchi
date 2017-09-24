package operation.inclusion;

import java.util.List;

import automata.BuchiWa;
import automata.IBuchiWa;
import automata.IStateWa;
import complement.IBuchiWaComplement;
import main.RunRabit;
import main.TaskInclusion;
import util.IPair;

public class BuchiInclusionRABIT implements IBuchiInclusion {

	private final IBuchiWa mFstOperand;
	private final IBuchiWa mSndOperand;
	private final TaskInclusion mTask;
	
	public BuchiInclusionRABIT(TaskInclusion task, IBuchiWa fstOp, IBuchiWa sndOp) {
		this.mTask = task;
		this.mFstOperand = fstOp;
		this.mSndOperand = sndOp;
	}
	@Override
	public IBuchiWa getFstBuchi() {
		// TODO Auto-generated method stub
		return mFstOperand;
	}

	@Override
	public IBuchiWa getSndBuchi() {
		// TODO Auto-generated method stub
		return mSndOperand;
	}

	@Override
	public IBuchiWaComplement getSndBuchiComplement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBuchiWa getBuchiDifference() {
		// TODO Auto-generated method stub
		return new BuchiWa(1);
	}

	@Override
	public Boolean isIncluded() {
		// TODO Auto-generated method stub
		try {
			return RunRabit.executeRabit(mFstOperand, mSndOperand, 10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	@Override
	public IPair<List<Integer>, List<Integer>> getCounterexampleWord() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPair<List<IStateWa>, List<IStateWa>> getCounterexampleRun() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "RABIT";
	}

}
