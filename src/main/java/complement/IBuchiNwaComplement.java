package complement;


import automata.IBuchiNwa;

public interface IBuchiNwaComplement extends IBuchiNwa {
	IBuchiNwa getOperand();
	
	DoubleDecker getDoubleDecker(int id);
	
	int getDoubleDeckerId(DoubleDecker decker);
}
