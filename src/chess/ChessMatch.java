package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVunerable;
	private ChessPiece promoted;
	
	private List <Piece> piecesOnTheBoard = new ArrayList<>();
	private List <Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		this.board = new Board(8,8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] chessPiece= new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				chessPiece[i][j] = (ChessPiece) board.piece(i,j);
			}
		}
		return chessPiece;
	}
	
	public int getTurn() {
		return turn;
	}
	public ChessPiece getPromoted() {
		return promoted;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	
	
	public ChessPiece getEnPassantVunerable() {
		return enPassantVunerable;
	}

	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition,ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source,target);
		Piece capturedPiece = makeMove(source,target);
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		promoted = null;
		if(movedPiece instanceof Pawn) {
			if((movedPiece.getColor() == Color.WHITE && target.getRow() == 0)||(movedPiece.getColor()== Color.BLACK && target.getRow() == 7)) {
				promoted = (ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("Q");
			}
		}
		check = (testCheck(opponent(currentPlayer)))? true : false;
		
		if(testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}else {
			nextTurn();
		}
		
		if(movedPiece instanceof Pawn && source.getRow() == target.getRow()-2 || source.getRow() == target.getRow()+2) {
			enPassantVunerable = movedPiece;
		}
		
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece piece = (ChessPiece)board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		
		piece.increaseMoveCount();
		
		board.placePiece(piece, target);
		if (capturedPiece!=null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		if (piece instanceof King && target.getColumn()==source.getColumn()+2 ) {
			Position sourceR = new Position(source.getRow(),source.getColumn()+3);
			Position targetR = new Position(source.getRow(),source.getColumn()+1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceR);
			board.placePiece(rook, targetR);
			rook.increaseMoveCount();
		}
		if (piece instanceof King && target.getColumn()==source.getColumn()-2 ) {
			Position sourceR = new Position(source.getRow(),source.getColumn()-4);
			Position targetR = new Position(source.getRow(),source.getColumn()-1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceR);
			board.placePiece(rook, targetR);
			rook.increaseMoveCount();
		}
		
		if(piece instanceof Pawn) {
			if (source.getColumn()!= target.getColumn()&& capturedPiece == null) {
				Position pawnPosition;
				if(piece.getColor()== Color.WHITE) {
					pawnPosition = new Position(target.getRow()+1,target.getColumn());
				}else {
					pawnPosition = new Position(target.getRow()-1,target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		if (p instanceof King && target.getColumn()==source.getColumn()+2 ) {
			Position sourceR = new Position(source.getRow(),source.getColumn()+3);
			Position targetR = new Position(source.getRow(),source.getColumn()+1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetR);
			board.placePiece(rook, sourceR);
			rook.decreaseMoveCount();
		}
		if (p instanceof King && target.getColumn()==source.getColumn()-2 ) {
			Position sourceR = new Position(source.getRow(),source.getColumn()-4);
			Position targetR = new Position(source.getRow(),source.getColumn()-1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetR);
			board.placePiece(rook, sourceR);
			rook.decreaseMoveCount();
		}
		
		if(p instanceof Pawn) {
			if (source.getColumn()!= target.getColumn()&& capturedPiece == enPassantVunerable) {
				ChessPiece pawn = (ChessPiece)board.removePiece(target);
				Position pawnPosition;
				if(p.getColor()== Color.WHITE) {
					pawnPosition = new Position(3,target.getColumn());
				}else {
					pawnPosition = new Position(4,target.getColumn());
				}
				board.placePiece(pawn, pawnPosition);
			}
		}
	}
	
	private void validateSourcePosition(Position source) {
		if(!board.thereIsAPiece(source)) {
			throw new ChessException("There isn't a piece at this position"); 
		}
		if(currentPlayer != ((ChessPiece)board.piece(source)).getColor()) {
			throw new ChessException("This is "+currentPlayer.toString()+" turn");
		}
		if(!board.piece(source).isThereAnyPossibleMove()) {
			throw new ChessException("There isn't a possible move for this piece");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to the target position");
		}
	}
	
	private void nextTurn() {
		turn ++;
		currentPlayer = (currentPlayer == Color.WHITE)?Color.BLACK :Color.WHITE;
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor()== color).collect(Collectors.toList());
		for (Piece piece : list) {
			if (piece instanceof King) {
				return (ChessPiece)piece;
			}
		}
		throw new IllegalStateException(color+" King wasn't found on board");
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor()== opponent(color)).collect(Collectors.toList());
		for (Piece piece : opponentPieces) {
			boolean[][] mat = piece.possibleMoves();
			if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor()== color).collect(Collectors.toList());
		for (Piece piece : list) {
			boolean [][] mat = piece.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (mat[i][j]) {
						Position source = ((ChessPiece)piece).getChessPosition().toPosition();
						Position target = new Position(i,j);
						Piece capturedPiece = makeMove(source,target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if(!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
		
	}
	
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	public ChessPiece replacePromotedPiece(String piece) {
		if (promoted == null) {
			throw new IllegalStateException("There isn't a piece to be promoted");
		}
		
		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);
		ChessPiece newPiece ;
		
		switch (piece) {
		case "Q":{
			newPiece = new Queen(board,promoted.getColor());
			break;
		}
		case "N":{
			newPiece = new Knight(board,promoted.getColor());
			break;
		}
		case "B":{
			newPiece = new Bishop(board,promoted.getColor());
			break;
		}
		case "R":{
			newPiece = new Rook(board,promoted.getColor());
			break;
		}
		default:
			throw new IllegalArgumentException("You can only add  Q, N, B, R. But not: " + piece);
		} 
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
	}
	
	private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('c', 1, new Knight(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board,Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Knight(board, Color.WHITE));
        placeNewPiece('g', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE,this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE,this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE,this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE,this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE,this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE,this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE,this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE,this));
        
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('c', 8, new Knight(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board,Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK,this));
        placeNewPiece('f', 8, new Knight(board, Color.BLACK));
        placeNewPiece('g', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK,this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK,this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK,this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK,this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK,this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK,this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK,this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK,this));
	}
}
