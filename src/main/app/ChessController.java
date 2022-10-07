import backend.pieces.ChessPiece;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChessController implements ChessDelegate, ActionListener, Runnable {

   private final ChessModel chessModel = new ChessModel();
   private final ChessView chessBoardPanel;

   private final JButton resetBtn;
   private final JButton serverBtn;
   private final JButton clientBtn;
   private PrintWriter printWriter;
   private Scanner scanner;

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
      frame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            printWriter.close();
            scanner.close();
         }
      });
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
            test(socket);
         }
      }
   }

   @Override
   @SneakyThrows
   public void run() {
      try (ServerSocket host = new ServerSocket(50_000)) {
         while (true) {
            Socket socket = host.accept();
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            test(socket);
            printWriter.println("Got you");
         }
      }
   }

   private void test(Socket socket) throws IOException {
      scanner = new Scanner(socket.getInputStream());
      while (scanner.hasNextLine()) {
         var moveStr = scanner.nextLine();
         var moveStrArr = moveStr.split(",");
         var fromCol = Integer.parseInt(moveStrArr[0]);
         var fromRow = Integer.parseInt(moveStrArr[1]);
         var toCol = Integer.parseInt(moveStrArr[2]);
         var toRow = Integer.parseInt(moveStrArr[3]);
         SwingUtilities.invokeLater(() -> {
            chessModel.movePiece(fromCol, fromRow, toCol, toRow);
            chessBoardPanel.repaint();
         });
      }
   }
}
