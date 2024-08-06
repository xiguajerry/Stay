/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.loader;


import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;


public class  JianShao  {
    private static JFrame frame = new JFrame();
    private static String version = "0.22";
    public static void main(String[] args) {

        try {
        String URLS = "http://47.106.126.97/mc/";

        frame = new JFrame("STAY Loader Interface");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        frame.setSize(616, 372);
        frame.setIconImage(ImageIO.read(new URL(URLS+"Stay.png")));


        Container con = frame.getContentPane();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        JButton stableButton = new JButton();
        JButton betaButton = new JButton();
        Random rand = new Random();

        stableButton.setOpaque(true);
        stableButton.setContentAreaFilled(false);
        stableButton.setBorderPainted(true);

        betaButton.setOpaque(true);
        betaButton.setContentAreaFilled(false);
        betaButton.setBorderPainted(true);

        stableButton.setToolTipText("Click to get information about stay");
        betaButton.setToolTipText("Click to check whether the loader is the latest version") ;

        // Load images and icons




                JLabel  backgroundPane = new JLabel(new ImageIcon(ImageIO.read(new URL(URLS+rand.nextInt(4)+".jpg"))));

            JLabel betaButtonIcon = new JLabel(new ImageIcon(ImageIO.read(new URL(URLS+"beta.png"))));
            JLabel  stayIcon = new JLabel(new ImageIcon(ImageIO.read(new URL(URLS+"stayhudlog.png"))));
            JLabel  Icon = new JLabel(new ImageIcon(ImageIO.read(new URL(URLS+"Stay.png"))));
            JLabel stableButtonIcon = new JLabel(new ImageIcon(ImageIO.read(new URL(URLS+"stable.png"))));
            JLabel breadIcon = new JLabel(new ImageIcon(ImageIO.read(new URL(URLS+"bread.png"))));

            con.add(stableButton);
            con.add(betaButton);
            con.add(betaButtonIcon);
            con.add(stayIcon);
            con.add(Icon);
            con.add(stableButtonIcon);
            int  bread = rand.nextInt(50);
            if (bread == 1) {
            con.add(breadIcon);
            }

            con.add(backgroundPane); // make sure that this is added last
            breadIcon.setBounds(200, 150, 128, 128);
            stableButtonIcon.setBounds(90, 245, 200, 50);
            stableButton.setBounds(90, 245, 200, 50);
            betaButtonIcon.setBounds(310, 245, 200, 50);
            betaButton.setBounds(310, 245, 200, 50);
            stayIcon.setBounds(236, 70, 128, 128);
            Icon.setBounds(30, 30, 32, 32);
            backgroundPane.setBounds(0, 0, 600, 355);

        betaButton.addActionListener(e -> {

            getFileContent("http://47.106.126.97/loader/bend.txt");
        });

        stableButton.addActionListener(e -> {
            JOptionPane.showConfirmDialog(frame, "Stay is the main development of CC, but bot, six and Moxi are also involved in the development"+"\r\n"+"This end uses Zori's framework and kamiblue's installer source code"+"\r\n"+"Contact information #cuican", "About stay by: CC", JOptionPane.YES_OPTION);
        });



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getFileContent(String fileUrl) {
        BufferedReader bf = null;
        String line = "";//文件每行内容
        String result  = "";//文件结果内容
        try {
            URL url  = new URL(fileUrl);
            //建立URL链接
            URLConnection conn = url.openConnection();
            //设置模拟请求头
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //开始链接
            conn.connect();
            //因为要用到URLConnection子类的方法，所以强转成子类
            HttpURLConnection urlConn = (HttpURLConnection) conn;
            //响应200
            if(urlConn.getResponseCode()==HttpURLConnection.HTTP_OK)
            {
                //字节或字符读取的方式太慢了，用BufferedReader封装按行读取
                bf =new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                while((line=bf.readLine())!=null)
                {
                    result+=line;
                }
                //result 获取得所有文件内容



                if(Double.valueOf(version).equals(Double.valueOf(result))){
                    JOptionPane.showConfirmDialog(frame, "Check succeeded. Your stay loader is the latest version:"+result, "Connection status: successful", JOptionPane.YES_OPTION);

                }else {
                  int and =   JOptionPane.showConfirmDialog(frame, "Check succeeded. Your stay loader is not the latest version.\r\n"+" Your version:"+version+"  Latest version:"+result+"\r\nUpdate?", "Connection status: successful", JOptionPane.YES_NO_OPTION);
                    if(and==JOptionPane.YES_OPTION){
                        URI uri = null;
                        try {
                            uri = new URI("http://47.106.126.97/loader/Stay-"+result+"-loader-obf.jar");
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        Desktop.getDesktop().browse(uri);
                    }
                }
                //通过已获取的文件内容   FTP上传至服务器新建文件中
            }else{
                JOptionPane.showConfirmDialog(frame, "Failed to connect to the server! overtime", "Unable to link to URL", JOptionPane.YES_OPTION);

                System.out.println("Unable to link to URL!");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            try
            {
                if(bf!=null){
                    bf.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }



//   public void init {
//        val stableButton = new JButton();
//        val betaButton = JButton();
//        val rand = Random();
//
//        stableButton.isOpaque = true;
//        stableButton.isContentAreaFilled = false;
//        stableButton.isBorderPainted = true;
//
//        betaButton.isOpaque = true;
//        betaButton.isContentAreaFilled = false;
//        betaButton.isBorderPainted = true;
//
//        stableButton.toolTipText = "This version of KAMI Blue is the latest major release";
//        betaButton.toolTipText = "A beta version of KAMI Blue, with frequent updates and fixes";
//
//        // Load images and icons
//        val backgroundImage = this.javaClass.getResource("/installer/0${rand.nextInt(4)}.jpg");
//        val backgroundPane = JLabel(ImageIcon(ImageIO.read(backgroundImage)));
//
//        val stableButtonImage = this.javaClass.getResource("/installer/stable.png")
//        val stableButtonIcon = JLabel(ImageIcon(ImageIO.read(stableButtonImage)))
//
//        val betaButtonImage = this.javaClass.getResource("/installer/beta.png")
//        val betaButtonIcon = JLabel(ImageIcon(ImageIO.read(betaButtonImage)))
//
//        val kamiImage = this.javaClass.getResource("/installer/kami.png")
//        val kamiIcon = JLabel(ImageIcon(ImageIO.read(kamiImage)))
//
//        val breadImage = this.javaClass.getResource("/installer/bread.png")
//        val breadIcon = JLabel(ImageIcon(ImageIO.read(breadImage)))
//
//        preferredSize = Dimension(600, 335)
//        layout = null
//
//        add(stableButton)
//        add(betaButton)
//        add(stableButtonIcon)
//        add(betaButtonIcon)
//        add(kamiIcon)
//
//        val bread = rand.nextInt(50)
//        if (bread == 1) {
//            add(breadIcon)
//        }
//
//        add(backgroundPane) // make sure that this is added last
//
//        stableButtonIcon.setBounds(90, 245, 200, 50)
//        stableButton.setBounds(90, 245, 200, 50)
//        betaButtonIcon.setBounds(310, 245, 200, 50)
//        betaButton.setBounds(310, 245, 200, 50)
//        kamiIcon.setBounds(236, 70, 128, 128)
//        breadIcon.setBounds(200, 150, 128, 128)
//        backgroundPane.setBounds(0, 0, 600, 355)
//
//        betaButton.addActionListener {
//            stableButton.isEnabled = false
//            betaButton.isEnabled = false
//            stableButton.isOpaque = false
//            betaButton.isOpaque = false
//            download(betaUrl)
//            notify("KAMI Blue $betaVersion has been installed", "Installed", JOptionPane.INFORMATION_MESSAGE)
//            exitProcess(0)
//        }
//
//        stableButton.addActionListener {
//            stableButton.isEnabled = false
//            betaButton.isEnabled = false
//            stableButton.isOpaque = false
//            betaButton.isOpaque = false
//            download(stableUrl)
//            notify("KAMI Blue $stableVersion has been installed", "Installed", JOptionPane.INFORMATION_MESSAGE)
//            exitProcess(0)
//        }
//    }
}
