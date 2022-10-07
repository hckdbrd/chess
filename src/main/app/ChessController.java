import backend.pieces.ChessPiece;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChessController implements ChessDelegate, ActionListener, Runnable {

   private final ChessModel chessModel = new ChessModel();
   private final ChessView chessBoardPanel;

   private JButton resetBtn, serverBtn, clientBtn;

   ChessController() {
      chessModel.reset();

      JFrame frame = new JFrame("Chess - GAMBIT");
      frame.setSize(600, 600);
      frame.setLocationRelativeTo(null);
      frame.setLayout(new BorderLayout());

      chessBoardPanel = new ChessView(this);
      frame.add(chessBoardPanel, BorderLayout.CENTER);

      JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      resetBtn = new JButton("Reset");
      resetBtn.addActionListener(this);
      buttonsPanel.add(resetBtn);
      serverBtn = new JButton("Host");
      serverBtn.addActionListener(this);
      buttonsPanel.add(serverBtn);
      clientBtn = new JButton("Connect");
      clientBtn.addActionListener(this);
      buttonsPanel.add(clientBtn);

      frame.add(buttonsPanel, BorderLayout.PAGE_END);

      frame.setVisible(true);
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
   }

   public static void main(String[] args) {
      new ChessController();
   }

   @Override
   public ChessPiece pieceAt(int col, int row) {
      return chessModel.pieceAt(col, row);
   }

   @Override
   public void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
      chessModel.movePiece(fromCol, fromRow, toCol, toRow);
      chessBoardPanel.repaint();
   }

   @Override
   @SneakyThrows
   public void actionPerformed(ActionEvent e) {
      Object source = e.getSource();
      if (resetBtn.equals(source)) {
         chessModel.reset();
         chessBoardPanel.repaint();
      } else if (serverBtn.equals(source)) {
         ExecutorService pool = Executors.newFixedThreadPool(1);
         pool.execute(this) ;
      } else if (clientBtn.equals(source)) {
         try (var socket = new Socket("localhost", 50_000)) {
            var in = new Scanner(socket.getInputStream());
            var moveStr = in.nextLine();
            var moveStrArr = moveStr.split(",");
            var fromCol = Integer.parseInt(moveStrArr[0]);
            var fromRow = Integer.parseInt(moveStrArr[1]);
            var toCol = Integer.parseInt(moveStrArr[2]);
            var toRow = Integer.parseInt(moveStrArr[3]);
            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  chessModel.movePiece(fromCol,fromRow,toCol,toRow);
                  chessBoardPanel.repaint();
               }
            });
         }
      }
   }

   @Override
   @SneakyThrows
   public void run() {
      try (ServerSocket host = new ServerSocket(50_000)) {
         while (true) {
            Socket socket = host.accept();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Got you");
         }
      }
   }
}
