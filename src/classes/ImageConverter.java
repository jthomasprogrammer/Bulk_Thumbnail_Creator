package classes;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageConverter {
	private File files[];
	private int resizedWidth, resizedHeight;
	private String fileType;
	/*
	 * Constructor for the ImageConverter Class.
	 * @param An array of image files.
	 * @param The width that the image files will be resized to.
	 * @param The height that the image files will be resized to.
	 * @param The file type the resized images will be saved to.
	 */
	public ImageConverter(File files[], int resizedWidth, int resizedHeight, String fileType){
		this.files = files;
		this.resizedWidth = resizedWidth;
		this.resizedHeight = resizedHeight;
		this.fileType = fileType;
	}

	/*
	 * Converts an array of files into an specified new width, length, and file type. The new images are renamed to resized_filename and placed in the same folder as the original image.
	 *@return A message detailing information about the conversion.
	 */
	public String convertImages(){
		//Creates a listed of the new resized Images.
		try{
			for(int i = 0; i < files.length; i++){
				File file = files[i];
				//Separates the file name from the type ending. 
				String filename = file.getName().substring(0, file.getName().lastIndexOf("."))+"."+fileType;
				String newFilename = file.getAbsolutePath().replace(file.getName(), "resized_"+filename);
				BufferedImage originalImage = ImageIO.read(file);
				int type = originalImage.getType();
				if(type == 0){
					type = BufferedImage.TYPE_INT_ARGB;
				}
				BufferedImage resizedImage = resizeImage(originalImage, type);
				ImageIO.write(resizedImage, fileType, new File(newFilename));
			}
			return "Files successfully converted.";
		}catch(IOException e){
			return "File conversion failed "+e.getMessage();
		}
	}

	/*
	 * Resizes an given image.
	 * @param The original image that this method will use for the conversion process.
	 * @param The image type of the original image.
	 * @return Returns a resized image.
	 */
	private BufferedImage resizeImage(BufferedImage originalImage, int type){
		BufferedImage resizedImage = new BufferedImage(resizedWidth, resizedHeight, type);
		Graphics2D g = resizedImage.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(originalImage, 0, 0, resizedWidth, resizedHeight, null);
		g.dispose();	
		g.setComposite(AlphaComposite.Src);
		return resizedImage;
	}
}
