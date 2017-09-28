package automata.wa;

import automata.AccGenBuchi;

public class GeneralizedBuchiWa extends BuchiWa {

    public GeneralizedBuchiWa(int alphabetSize) {
        super(alphabetSize);
    }
    
    @Override
    public AccGenBuchi getAcceptance() {
        if(acc == null) {
            acc = new AccGenBuchi(getFinalStates());
        }
        return (AccGenBuchi) acc;
    }
}
