/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.io.InputStream;
import java.io.Serializable;
import javafx.scene.image.Image;

/**
 *
 * @author jungn
 */
public class ImageRMI extends Image implements Serializable{
	
	public ImageRMI(InputStream is) {
		super(is);
	}
	
}
