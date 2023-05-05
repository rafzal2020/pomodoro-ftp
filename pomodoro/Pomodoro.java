import java.io.File;
import java.io.IOException;
 
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Pomodoro implements ActionListener{
    JFrame frame = new JFrame("Pomodoro", null);

    JButton start = new JButton("START");
    JButton reset = new JButton("RESET");
    JButton increaseMinute = new JButton("^");
    JButton decreaseMinute = new JButton("⌄");
    JButton increaseSecond = new JButton("^");
    JButton decreaseSecond = new JButton("⌄");

    JLabel timeLabel = new JLabel();
    JLabel dialogue = new JLabel();
    JLabel pomodoroCount = new JLabel();

    // timer
    boolean started = false;
    boolean isBreak = false;
    boolean isLongBreak = false;
    int elapsed = 0;
    int seconds = 0;
    int minutes = 25;
    int count = 0;
    int result;
    String secondString = String.format("%02d", seconds);
    String minuteString = String.format("%02d", minutes);
    String alarm = "C:/Users/Rayaan Afzal/cs-stuff/randomstudywork/pomodoro/notif.wav";
    AlarmPlayer newAlarm = new AlarmPlayer();

    // reset timer to original time
    int originalSeconds = 0;
    int originalMinutes = 25;

    Timer timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (seconds == 0) {
                seconds = 60;
                if (minutes != 0) {
                    minutes -= 1;
                }
            } seconds--;

            if (minutes == 0 && seconds == 0) {
                if (isBreak) {
                    isBreak = false;
                    dialogue.setText("Time to Focus.");
                    minutes = originalMinutes;
                    seconds = originalSeconds;
                    dialogue.setBounds(90, -10, 150, 100);
                    increaseMinute.setEnabled(true);
                    decreaseMinute.setEnabled(true);
                    increaseSecond.setEnabled(true);
                    decreaseSecond.setEnabled(true);
                } else if (!isBreak) {
                    count+=1;
                    pomodoroCount.setText("Completed Pomodoros: " + count);
                    if (count%4 == 0 && count != 0) {
                        isBreak = true;
                        dialogue.setText("Time for a long break.");
                        dialogue.setBounds(60, -10, 200, 100);
                        minutes = 15;
                        seconds = 0;
                        increaseMinute.setEnabled(false);
                        decreaseMinute.setEnabled(false);
                        increaseSecond.setEnabled(false);
                        decreaseSecond.setEnabled(false);
                    } else {
                    isBreak = true;
                    dialogue.setText("Time for a Break.");
                    
                    minutes = 5;
                    seconds = 0;
                    dialogue.setBounds(80, -10, 150, 100);
                    increaseMinute.setEnabled(false);
                    decreaseMinute.setEnabled(false);
                    increaseSecond.setEnabled(false);
                    decreaseSecond.setEnabled(false);
                    }
                } 
                
                timer.stop();
                newAlarm.play(alarm);
                started = false;
                start.setText("START");
                
                
                JOptionPane.showMessageDialog(frame, "Timer is up!");

                secondString = String.format("%02d", seconds);
                minuteString = String.format("%02d", minutes);
            }
            secondString = String.format("%02d", seconds);
            minuteString = String.format("%02d", minutes);

            timeLabel.setText(minuteString + ":" + secondString);
        }
    });

    public Pomodoro() {
        dialogue.setText("Time to Focus.");
        dialogue.setBounds(90, -10, 200, 100);
        dialogue.setFont(new Font("Verdana", Font.BOLD, 15));

        pomodoroCount.setText("Completed Pomodoros: " + count);
        pomodoroCount.setBounds(80,275, 200, 50);

        timeLabel.setText(minuteString + ":" + secondString);
        timeLabel.setBounds(75, 50,150,100);
        timeLabel.setFont(new Font("Verdana", Font.BOLD, 30));
        timeLabel.setBorder(BorderFactory.createBevelBorder(1));
        timeLabel.setOpaque(true);
        timeLabel.setHorizontalAlignment(JTextField.CENTER);

        start.addActionListener(this);
        start.setBounds(100,175,100,50);

        reset.addActionListener(this);
        reset.setBounds(100,225,100,50);

        increaseMinute.addActionListener(this);
        increaseMinute.setBounds(50, 50, 25, 50);
        decreaseMinute.addActionListener(this);
        decreaseMinute.setBounds(50, 100, 25, 50);
        increaseSecond.addActionListener(this);
        increaseSecond.setBounds(225, 50, 25, 50);
        decreaseSecond.addActionListener(this);
        decreaseSecond.setBounds(225, 100, 25, 50);

        frame.add(dialogue);
        frame.add(timeLabel);
        frame.add(start);
        frame.add(reset);
        frame.add(pomodoroCount);
        frame.add(increaseMinute);
        frame.add(decreaseMinute);
        frame.add(increaseSecond);
        frame.add(decreaseSecond);
        frame.setTitle("Pomodoro");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300,400);
		frame.setLayout(null);
		frame.setVisible(true);
    }

    @ Override
    public void actionPerformed(ActionEvent x) {
        if (x.getSource() == start) {
            if (!started) {
                started = true;
                timer.start();
                start.setText("STOP");
            } else {
                started = false;
                timer.stop();
                start.setText("START");
            }
        }
        if (x.getSource() == reset) {
            timer.stop();
            start.setText("START");
            minutes = originalMinutes;
            seconds = originalSeconds;
            secondString = String.format("%02d", seconds);
            minuteString = String.format("%02d", minutes);
            timeLabel.setText(minuteString + ":" + secondString);
        }
        if (x.getSource() == increaseMinute) {
            minutes += 1;
            originalMinutes += 1;
            minuteString = String.format("%02d", minutes);
            timeLabel.setText(minuteString + ":" + secondString);
        }
        if (x.getSource() == decreaseMinute) {
            if (minutes != 0) {minutes -= 1; originalMinutes -= 1;}
            minuteString = String.format("%02d", minutes);
            timeLabel.setText(minuteString + ":" + secondString);
        }
        if (x.getSource() == increaseSecond) {
            seconds += 1;
            originalSeconds += 1;
            if (seconds >= 59) {
                seconds = 0;
                originalSeconds = 0;
            }
            secondString = String.format("%02d", seconds);
            timeLabel.setText(minuteString + ":" + secondString);
        }
        if (x.getSource() == decreaseSecond) {
            seconds -= 1;
            originalSeconds -= 1;
            if (seconds <= 0) {
                seconds -= 1;
                originalSeconds -=1;
                seconds = 59;
                originalSeconds = 59;
            }
            secondString = String.format("%02d", seconds);
            timeLabel.setText(minuteString + ":" + secondString);
        }
    }

    public class AlarmPlayer implements LineListener {
        /**
     * this flag indicates whether the playback completes or not.
     */
    boolean playCompleted;
     
    /**
     * Play a given audio file.
     * @param audioFilePath Path of the audio file.
     */
    void play(String audioFilePath) {
        File audioFile = new File(audioFilePath);
 
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip audioClip = (Clip) AudioSystem.getLine(info);
 
            audioClip.addLineListener(this);
            audioClip.open(audioStream);
            audioClip.start();
             
            while (!playCompleted) {
                // wait for the playback completes
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            audioClip.close();
            
             
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }

         
    }
     
    /**
     * Listens to the START and STOP events of the audio line.
     */
    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
         
        if (type == LineEvent.Type.START) {
            System.out.println("Playback started.");
             
        } else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
            System.out.println("Playback completed.");
        }
 
    }
 
    }

    public static void main(String[] args) {
        new Pomodoro();
    }
}
