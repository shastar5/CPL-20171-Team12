package com.sample.cep;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Rxtx
{                
	static int writeValue = 0;      
	static String readValue;
    public Rxtx()
    {
        super();
    }
    
    void connect (String portName) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            //클占쏙옙占쏙옙 占싱몌옙占쏙옙 占식븝옙占쌘뤄옙 占쏙옙占쏙옙臼占� 占쏙옙트 占쏙옙占쏙옙
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                //占쏙옙트 占쏙옙占쏙옙(占쏙옙탉撻占� 占쏙옙占쏙옙. 占썩본 9600占쏙옙占쏙옙 占쏙옙占�)
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                //Input,OutputStream 占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙 占쏙옙占쏙옙
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                
                 //占싻깍옙, 占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쌜듸옙
                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    /** */
    //占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙
    public static class SerialReader implements Runnable 
    {
        InputStream in;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[100];
            int len = -1;
            try
            {
                while ( ( len = this.in.read(buffer)) >-1 )
                {
                	readValue = new String(buffer,0,len);
                	System.out.print(readValue);
        			Thread.sleep(1000);
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}            
        }
    }
   
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {
                while (writeValue!=-1 )
                {
                    this.out.write(writeValue);
                    writeValue = -1;
                }       
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }
    
    public static void main ( String[] args )
    {
        try
        {
            (new Rxtx()).connect("COM7"); //占쌉뤄옙占쏙옙 占쏙옙트占쏙옙 占쏙옙占쏙옙
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}