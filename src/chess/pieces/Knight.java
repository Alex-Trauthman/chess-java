package chess.pieces;

import boardGame.Board;
import boardGame.Position;
import chess.ChessPiece;
import chess.Color;

public class Knight extends ChessPiece{

	public Knight(Board board, Color color) {
		super(board, color);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "H";
	}
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position aux = new Position(0,0);
		
		//1up and 2left 
		aux.setValues(position.getRow()-1, position.getColumn()-2);
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		//1down and 2left
		aux.setValues(position.getRow()+1, position.getColumn()-2);
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		//1down and 2right
		aux.setValues(position.getRow()+1, position.getColumn()+2);
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		//1up and 2right
		aux.setValues(position.getRow()-1, position.getColumn()+2);
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		//2down and 1left
		aux.setValues(position.getRow()+2, position.getColumn()-1);
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		//2up and 1right
		aux.setValues(position.getRow()-2, position.getColumn()+1);
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		//2down and 1right
		aux.setValues(position.getRow()+2, position.getColumn()+1);
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		//2up and 1 left
		aux.setValues(position.getRow()-2, position.getColumn()-1);
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		
		return mat;
	}
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return (p== null || p.getColor()!= getColor());
	}
}
