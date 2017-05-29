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
            //Ŭ���� �̸��� �ĺ��ڷ� ����Ͽ� ��Ʈ ����
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                //��Ʈ ����(��żӵ� ����. �⺻ 9600���� ���)
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                //Input,OutputStream ���� ���� �� ����
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                
                 //�б�, ���� ������ �۵�
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
    //������ ����
    public static class SerialReader implements Runnable 
    {
        InputStream in;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 )
                {
                	readValue = new String(buffer,0,len);
                }
            }
            catch ( IOException e )
            {
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
            (new Rxtx()).connect("COM7"); //�Է��� ��Ʈ�� ����
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}