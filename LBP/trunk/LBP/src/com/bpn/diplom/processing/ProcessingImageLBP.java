package com.bpn.diplom.processing;

import com.bpn.diplom.lbp.*;
import com.bpn.diplom.dao.*;
import com.bpn.diplom.gui.*;
import com.bpn.diplom.gui.utils.GUITools;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.googlecode.javacv.cpp.opencv_core.*;


public class ProcessingImageLBP //implements ProcessingImage
{
	
	private int countBlock = 7; 
	private FacesDetection facesDetector = new FacesDetection();
	private LBPImage faceVectorBuilder8;
	private LBPImage faceVectorBuilder12;
	private LBPImage faceVectorBuilder16;

	
	
	public ProcessingImageLBP(){
		super();
		faceVectorBuilder8  = new LBPImage(LBPImage.AROUND_8_POINTS, countBlock);
		faceVectorBuilder12 = new LBPImage(LBPImage.AROUND_12_POINTS, countBlock);
		faceVectorBuilder16 = new LBPImage(LBPImage.AROUND_16_POINTS, countBlock);
		
	}
	
	
//	@Override
//	public IplImage processingImage(IplImage image) {
//		return IplImage.createFrom(processingImage(image.getBufferedImage()));
//	}	
	

	//@Override
	public EntityLBPUser processingFaceImage(BufferedImage faceImage, Map<String, Image> imagesForShowProcessing){
		EntityLBPUser user = null;
		BufferedImage face = null;
		try{
			face = createPreparingFaceImage(faceImage, imagesForShowProcessing);
			new ShowImageSimple(face);
			new ShowImageSimple(faceImage);
			
			user = new EntityLBPUser();
			user.setVector8(faceVectorBuilder8.getFaceVectorByRaster(face.getData()));
			user.setVector12(faceVectorBuilder12.getFaceVectorByRaster(face.getData()));
			user.setVector16(faceVectorBuilder16.getFaceVectorByRaster(face.getData()));
			user.setImageFaceArea(faceImage);
			user.setImageFace(face);

			drawFaceGrid(faceImage);
			drawPriority(faceImage);
		}catch(Throwable c){
			c.printStackTrace();
		}
		return user;
	}
	
	//@Override
	public List<EntityLBPUser> processingImage(BufferedImage image, Map<String, Image> imagesForShowProcessing){
		List<Image> faces = facesDetector.detect(image, true, imagesForShowProcessing);
		List<EntityLBPUser> users = new ArrayList<EntityLBPUser>();
		for(Image face : faces){
			users.add(processingFaceImage((BufferedImage) face, imagesForShowProcessing));
		}
		return users;
	}
	
	
	private BufferedImage createPreparingFaceImage(BufferedImage face, Map<String, Image> imagesForShowProcessing){
		int widthOld = face.getWidth(null);
		int heightOld = face.getHeight(null);

		final double width = 500d;
		final double scale = width / (double)widthOld;
		
		if (face.getType() != BufferedImage.TYPE_INT_RGB) {
			BufferedImage face2 = new BufferedImage(widthOld, heightOld, BufferedImage.TYPE_INT_RGB);
			face2.getGraphics().drawImage(face, 0, 0, null);
			face = face2;
		}
		
		AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
		AffineTransformOp aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		

		BufferedImage result = new BufferedImage((int)(widthOld*scale), (int)(heightOld*scale), BufferedImage.TYPE_BYTE_GRAY);
		((Graphics2D)(result.getGraphics())).drawImage(face, aop, 0, 0);

		BufferedImage scaledImg = new BufferedImage((int)(widthOld*scale), (int)(heightOld*scale), BufferedImage.TYPE_INT_RGB);
		((Graphics2D)(scaledImg.getGraphics())).drawImage(face, aop, 0, 0);
		imagesForShowProcessing.put("Обличчя приведене до стандартної ширини("+width+"px)", scaledImg);
		
//		BufferedImage resultShow = new BufferedImage(scaledImg.getWidth(), scaledImg.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//		((Graphics2D)(resultShow.getGraphics())).drawImage(scaledImg, 0, 0, null);
		imagesForShowProcessing.put("Обличчя приведене до відтінків сірого", result);
		
		return result;
	} 
	
	
	private void drawFaceGrid(BufferedImage img){
		Graphics2D g = (Graphics2D) img.getGraphics();
		int difX = img.getWidth() / countBlock;
		int difY = img.getHeight() / countBlock;
		for(int i = 0; i < countBlock; i++){
			g.drawLine(0, difY * i, img.getWidth(), difY * i);
			g.drawLine(difX * i, 0, difX * i, img.getHeight());
		}
//		g.draw(face);	
	}
	
	
//	private void drawFacesGrid(Rectangle[] faces, Graphics2D g){
//		for(Rectangle face : faces){
//			int difX = face.width / countBlock;
//			int difY = face.height / countBlock;
//			for(int i = 0; i < countBlock; i++){
//				g.drawLine(face.x, face.y + difY * i, face.x + face.width, face.y + difY * i);
//				g.drawLine(face.x + difX * i, face.y, face.x + difX * i, face.y + face.height);
//			}
//			g.draw(face);	
//		}
//	}
	private void drawFacesGrid(Rectangle face, Graphics2D g){
		int difX = face.width / countBlock;
		int difY = face.height / countBlock;
		for(int i = 0; i < countBlock; i++){
			g.drawLine(face.x, face.y + difY * i, face.x + face.width, face.y + difY * i);
			g.drawLine(face.x + difX * i, face.y, face.x + difX * i, face.y + face.height);
		}
		g.draw(face);	
	}
	
	
	private void drawPriority(BufferedImage img){
		Graphics2D g = (Graphics2D) img.getGraphics();
		int difX = img.getWidth() / countBlock;
		int difY = img.getHeight() / countBlock;
		Color old = g.getColor();
		g.setColor(Color.RED);
		for(int i = 0; i < countBlock*countBlock; i++){
			g.drawString(String.valueOf(LBPImage.PRIORITY_7_BLOCKS[i]), difX/2 + difX*(i%countBlock), difY/2 + difY*(i/countBlock));
		}
		g.setColor(old);
	}
	private void drawPriority(Rectangle face, Graphics2D g){
		int difX = face.width / countBlock;
		int difY = face.height / countBlock;
		Color old = g.getColor();
		g.setColor(Color.RED);
		for(int i = 0; i < countBlock*countBlock; i++){
			g.drawString(String.valueOf(LBPImage.PRIORITY_7_BLOCKS[i]), face.x + difX/2 + difX*(i%countBlock), face.y + difY/2 + difY*(i/countBlock));
		}
		g.setColor(old);
	}
	
	
//	private void drawPanel(Graphics2D g) {
//		g.setColor(Color.RED);
//		g.drawString("    min: " + minDistance, 0, 20);
//		g.drawString("    max: " + maxDistance, 0, 40);
//		g.drawString("current: " + currentDistance, 0, 80);
//		if(storedFaceVector != null)
//			g.drawString("lngth: " + storedFaceVector.length, 0, 100);
////		g.drawRect((int)focus.getX(), (int)focus.getY(), (int)focusSize.getWidth(), (int)focusSize.getHeight());
//	}

//
//	/**
//	 * возвращает вектор признаков лица
//	 * @return
//	 */
//	private synchronized int[] preprocessingCalculation(){
//		BufferedImage b = new BufferedImage(face.width,face.height, 5);
////		BufferedImage b = new BufferedImage(640,532, 5);
//		b.setData(frame.getData(face));
//		frame.getSubimage(face.x, face.y, face.width, face.height).copyData(b.getRaster());
//		BufferedImage faceRaster = Util.resizeImage(b, null);
////		b.setData(faceRaster.getData());
////		faceRaster.copyData(b.getRaster());
//		
////		Graphics2D g = (Graphics2D) frame.getGraphics();
////		g.drawImage(faceRaster, null, 0, 150);
//		return faceVectorBuilder.getFaceVectorByRaster(faceRaster.getData());
//	} 
//	
//	
//	
//	private void preprocessingViews(){
//		Graphics2D g = (Graphics2D) frame.getGraphics();
//		Util.imgToGray(frame.getRaster(), face);
//		drawPriority(g);
//	}

	
	
}
