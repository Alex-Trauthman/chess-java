package chess.pieces;

import boardGame.Board;
import boardGame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece{

	private ChessMatch chessMatch;
	
	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch=chessMatch;
	}
	@Override
	public String toString() {
		return "K";
	}
	
	private boolean testRookCastling(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount()==0;
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
		
		//Castling
		if((getMoveCount() ==0) && !chessMatch.getCheck()) {
			//King side rook
			Position aux1= new Position(position.getRow(),position.getColumn()+3);
			if(testRookCastling(aux1)) {
				Position p1 = new Position(position.getRow(),position.getColumn()+1);
				Position p2 = new Position(position.getRow(),position.getColumn()+2);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
					mat[position.getRow()][position.getColumn()+2] = true;
				}
			}
			//Queen side rook
			Position aux2= new Position(position.getRow(),position.getColumn()-4);
			if(testRookCastling(aux2)) {
				Position p1 = new Position(position.getRow(),position.getColumn()-1);
				Position p2 = new Position(position.getRow(),position.getColumn()-2);
				Position p3 = new Position(position.getRow(),position.getColumn()-3);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3)==null) {
					mat[position.getRow()][position.getColumn()-2] = true;
				}
			}
			
		}
		
		return mat;
	}
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return (p== null || p.getColor()!= getColor());
	}
}
