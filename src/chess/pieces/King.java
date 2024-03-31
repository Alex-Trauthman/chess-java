package chess.pieces;

import boardGame.Board;
import boardGame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece{

	public King(Board board, Color color) {
		super(board, color);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "K";
	}
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position aux = new Position(0,0);
		
		//above 
		aux.setValues(position.getRow()-1, position.getColumn());
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		//below
		aux.setValues(position.getRow()+1, position.getColumn());
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		//left
		aux.setValues(position.getRow(), position.getColumn()-1);
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		//right
		aux.setValues(position.getRow(), position.getColumn()+1);
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		//upper left 
		aux.setValues(position.getRow()-1, position.getColumn()-1);
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		//upper right
		aux.setValues(position.getRow()-1, position.getColumn()+1);
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		//lower left
		aux.setValues(position.getRow()+1, position.getColumn()-1);
		if (getBoard().positionExists(aux)&& canMove(aux)) {
			mat[aux.getRow()][aux.getColumn()] = true;
		}
		//lower right
		aux.setValues(position.getRow()+1, position.getColumn()+1);
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
