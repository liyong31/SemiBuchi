package operation.inclusion;

import java.util.List;

import automata.IBuchi;
import automata.IState;
import main.RunRabit;
import main.TaskInfo;
import util.IPair;

public class BuchiInclusionRABIT implements IBuchiInclusion {

	private final IBuchi mFstOperand;
	private final IBuchi mSndOperand;
	private final TaskInfo mTask;
	
	public BuchiInclusionRABIT(TaskInfo task, IBuchi fstOp, IBuchi sndOp) {
		this.mTask = task;
		this.mFstOperand = fstOp;
		this.mSndOperand = sndOp;
	}
	@Override
	public IBuchi getFstBuchi() {
		// TODO Auto-generated method stub
		return mFstOperand;
	}

	@Override
	public IBuchi getSndBuchi() {
		// TODO Auto-generated method stub
		return mSndOperand;
	}

	@Override
	public IBuchi getSndBuchiComplement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBuchi getBuchiDifference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isIncluded() {
		// TODO Auto-generated method stub
		try {
			return RunRabit.executeRabit(mFstOperand, mSndOperand, mTask.getTimeBound());
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
	public IPair<List<IState>, List<IState>> getCounterexampleRun() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "RABIT";
	}

}
