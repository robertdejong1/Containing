/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Exceptions;

/**
 *
 * @author Robert
 */
public class InvalidVehicleException extends Exception{
    public InvalidVehicleException(String msg){
        super(msg);
    }
}
