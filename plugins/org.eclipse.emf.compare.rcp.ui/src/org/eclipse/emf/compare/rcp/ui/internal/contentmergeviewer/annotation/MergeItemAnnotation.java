/**
 * 
 */
package org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.annotation;

import org.eclipse.swt.graphics.Image;

/**
 * @author Alexandra Buzila
 */
public class MergeItemAnnotation {

	private String description;

	private String header;

	private Image image;

	public MergeItemAnnotation(Image image, String header, String description) {
		this.image = image;
		this.header = header;
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header
	 *            the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}

}
