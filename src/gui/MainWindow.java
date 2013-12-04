package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import classes.ImageConverter;


public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6591463743507048007L;
	private JPanel panel = new JPanel();
	private ArrayList<File> fileList;
	public MainWindow(){
		initUI();
		fileList = new ArrayList<File>();
	}

	private void initUI(){
		panel = new JPanel(); 
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JButton addFilesButton = new JButton("Add file(s) to be converted");

		panel.add(addFilesButton);

		JLabel convertingLabel = new JLabel("Converting the following files");
		panel.add(convertingLabel);
		final JTextArea fileBox = new JTextArea("");
		fileBox.setEditable(false);
		JScrollPane scroll = new JScrollPane (fileBox);
		scroll.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

		panel.add(scroll);

		JPanel newSizePanel = new JPanel();
		newSizePanel.setLayout(new BoxLayout(newSizePanel, BoxLayout.X_AXIS));
		JLabel sizeLabel = new JLabel("Enter new size: ");
		newSizePanel.add(sizeLabel);
		JLabel widthLabel = new JLabel("Width: ");
		newSizePanel.add(widthLabel);
		final JTextField widthBox = new JTextField(24);
		widthBox.setBackground(Color.WHITE);
		newSizePanel.add(widthBox);
		JLabel heightLabel = new JLabel("Height: ");
		newSizePanel.add(heightLabel);
		final JTextField heightBox = new JTextField(24);
		heightBox.setBackground(Color.WHITE);
		newSizePanel.add(heightBox);

		panel.add(newSizePanel);

		JPanel newTypePanel = new JPanel();
		newTypePanel.setLayout(new BoxLayout(newTypePanel, BoxLayout.X_AXIS));
		JLabel fileTypeLabel = new JLabel("Enter the file type that you want the created images to have: ");
		newTypePanel.add(fileTypeLabel);
		final JTextField fileTypeBox = new JTextField(24);
		fileTypeBox.setBackground(Color.WHITE);
		newTypePanel.add(fileTypeBox);

		panel.add(newTypePanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		JButton convertButton = new JButton("Create the thumbnails!");
		buttonPanel.add(convertButton);
		JButton clearListButton = new JButton("Clear the file list");
		buttonPanel.add(clearListButton);

		panel.add(buttonPanel);

		/*
		 * Button Listener's
		 */
		
		/*
		 * Adds files to the class variable fileList and updates the fileBox.
		 */
		addFilesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fc = new JFileChooser();
				//Lets the user select multiple files.
				fc.setMultiSelectionEnabled(true);
				int returnVal = fc.showOpenDialog(MainWindow.this);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					File files[] = fc.getSelectedFiles();
					for(int i = 0; i < files.length; i++){
						File file = files[i];
						fileList.add(file);
						String filename = file.getName();
						//Update the files box. 
						String currentText = fileBox.getText();
						if(currentText.length() != 0){
							fileBox.setText(currentText+", "+filename);
						}else{
							fileBox.setText(filename);
						}

					}
					
				}
			}
		});
		
		/*
		 * Creates the new thumbnails in the folder where the original image is located.
		 */
		convertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(widthBox.getText().length() == 0 || heightBox.getText().length() == 0 || fileTypeBox.getText().length() == 0){
					JOptionPane.showMessageDialog(null, "Either the width, height, or file type field is empty.");
				}else if(fileList.isEmpty()){
					JOptionPane.showMessageDialog(null, "Add some images to convert.");
				}else{
					//TODO make into a thread by calling hello thread and putting anything below here into the hello thread
					int width = Integer.parseInt(widthBox.getText());
					int height = Integer.parseInt(heightBox.getText());
					String fileType = fileTypeBox.getText();
					File[] fileArr = new File[fileList.size()];
					fileArr = fileList.toArray(fileArr);
					ImageConverter imageConverter = new ImageConverter(fileArr, width, height, fileType);
					ConversionThread thread = new ConversionThread (imageConverter);
					thread.start();
				}
			}
		});
		
		/*
		 * Clear the file box of any listed files.
		 */
		clearListButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				fileList.clear();
				fileBox.setText("");
			}
		});
		
		add(panel);
		pack();
		setTitle("Bulk Thumbnail Creator");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/*
	 * The conversion thread which handles converting images.
	 * @param Image Converter which contains the conversion settings specified by the user.
	 */
	public class ConversionThread extends Thread {
		ImageConverter imageConverter;
		public ConversionThread(ImageConverter imageConverter){
			this.imageConverter = imageConverter;
		}
	    public void run() {
	        String result = imageConverter.convertImages();
	        JOptionPane.showMessageDialog(null, result);
	        //Thread sleeps for 4  milis.
	        try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				return;
			}
	    }

	}
	

}
