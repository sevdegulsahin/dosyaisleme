package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.net.URL; // URL sınıfını import et

public class GUI {
    public static void GUIPlay() {
        // Swing GUI

        JFrame frame1 = new JFrame("paralel dosya");
        frame1.setSize(750, 750);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JFrame frame2 = new JFrame("mail");
        frame2.setSize(400, 200);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblEmail = new JLabel("Email adresinizi giriniz:");
        lblEmail.setBounds(20, 0, 200, 100);

        JTextArea txtEmail = new JTextArea();
        txtEmail.setBounds(200, 39, 200, 18);

        JButton btnGiris = new JButton("Giriş");
        btnGiris.setBounds(200, 100, 100, 25);

        JTextArea txtKelime = new JTextArea();
        txtKelime.setBounds(230, 25, 250, 20);

        JLabel lblKelime = new JLabel("kelime sayısı:");
        lblKelime.setBounds(140, 28, 200, 100);

        JLabel lblKarakter = new JLabel("karakter sayısı:");
        lblKarakter.setBounds(400, 28, 200, 100);

        JTextArea textArea = new JTextArea();
        textArea.setBounds(0, 100, 750, 680);

        JButton btnSearch = new JButton();
        btnSearch.setBackground(Color.white);
        btnSearch.setForeground(Color.PINK);
        btnSearch.setBounds(535, 10, 50, 60); // Set button size to 100x50

        // Load the image for the "search" button
        String searchButtonImagePath = "https://w1.pngwing.com/pngs/950/465/png-transparent-search-icon-search-icon-search-line-icon-icon-pink-magenta-circle.png";
        try {
            URL searchButtonUrl = new URL(searchButtonImagePath);
            ImageIcon searchButtonIcon = new ImageIcon(searchButtonUrl);
            Image searchButtonImage = searchButtonIcon.getImage();
            Image scaledSearchButtonImage = searchButtonImage.getScaledInstance(50, 60, Image.SCALE_SMOOTH);
            btnSearch.setIcon(new ImageIcon(scaledSearchButtonImage));
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textArea.getText();
                String[] words = text.split("\\s+");
                lblKelime.setText("kelime sayısı:" + (words.length));
                lblKarakter.setText("karakter sayısı:" + (text.length() - 1));

                String emailInput = txtEmail.getText(); // Kullanıcının girdiği e-posta adresi

                // Mail gönderme işlemi
                final String userName = "suedanursarican233@gmail.com"; // Gönderen e-posta adresi
                final String password = "t q s f h q s d s x l w j c v q"; // Gönderen e-posta şifresi

                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password);
                    }
                });

                try {
                    MimeMessage message = new MimeMessage(session);
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailInput, true));
                    message.setSubject("Mail Gönderildi");
                    message.setText("Mail gönderildi.");
                    System.out.println("Mail Gönderiliyor...");
                    Transport.send(message);
                    System.out.println("Mail Başarıyla Gönderildi....");
                } catch (MessagingException me) {
                    System.out.println("Exception: " + me);
                }
            }
        });

        JLabel lblSearch = new JLabel("search word");
        lblSearch.setBounds(140, 0, 100, 70);

        JFileChooser fileChooser = new JFileChooser();

        // Load the image for the "seç" button
        String selectButtonImagePath = "https://w7.pngwing.com/pngs/1015/894/png-transparent-computer-icons-directory-%E5%9B%BE%E6%A0%87-purple-image-file-formats-violet.png";
        try {
            URL selectButtonUrl = new URL(selectButtonImagePath);
            ImageIcon selectButtonIcon = new ImageIcon(selectButtonUrl);
            Image selectButtonImage = selectButtonIcon.getImage();
            Image scaledSelectButtonImage = selectButtonImage.getScaledInstance(70, 60, Image.SCALE_SMOOTH);
            JButton btnChooseFile = new JButton(); // Create the button here
            btnChooseFile.setBounds(20, 15, 70, 60); // Set button size to 100x100
            btnChooseFile.setIcon(new ImageIcon(scaledSelectButtonImage));
            btnChooseFile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int returnVal = fileChooser.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        try {
                            textArea.read(new FileReader(file.getAbsolutePath()), null);
                        } catch (IOException ex) {
                            System.out.println("Dosyaya ulaşırken bir hata oluştu: " + file.getAbsolutePath());
                        }
                    } else {
                        System.out.println("Seçim kullanıcı tarafından iptal edildi.");
                    }
                }
            });
            frame1.add(btnChooseFile); // Add the button to the frame
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnGiris.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame2.setVisible(false);
                frame1.add(lblSearch);
                frame1.add(txtKelime);
                frame1.add(btnSearch);
                frame1.add(lblKelime);
                frame1.add(lblKarakter);
                frame1.add(textArea);
                frame1.setLayout(null);
                frame1.setVisible(true);
            }
        });

        frame2.add(lblEmail);
        frame2.add(txtEmail);
        frame2.add(btnGiris);
        frame2.setLayout(null);
        frame2.setVisible(true);
    }
}

