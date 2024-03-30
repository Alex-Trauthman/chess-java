package boardGame;

public class Piece {
	protected Position posistion;
	private Board board;
	
	public Piece(Board board, Position position) {
		super();
		this.posistion = null;
		this.board = board;
	}
	
	protected Board getBoard() {
		return board;
	}

	
	
}
