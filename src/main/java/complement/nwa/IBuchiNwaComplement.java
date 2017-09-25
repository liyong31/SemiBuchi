package complement.nwa;


import automata.nwa.IBuchiNwa;

public interface IBuchiNwaComplement extends IBuchiNwa {
	IBuchiNwa getOperand();
	
	DoubleDecker getDoubleDecker(int id);
	
	int getDoubleDeckerId(DoubleDecker decker);
}
