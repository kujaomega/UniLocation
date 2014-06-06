package com.unilocation.unilocation;





import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MainActivity extends Activity {

	TextView textView;
	Thread thread;
	String TAG="Error run";
	List<ScanResult> results;

	static Connection conexionMySQL;
	static String SQLEjecutar;
	String resultadoSQL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.hello_world);
		textView.setText("hello World");
	
		Thread t = new Thread() {

			  @Override
			  public void run() {
			    try {
			    	Looper.prepare();
			      while (!isInterrupted()) {
			    	  
			        Thread.sleep(10000);
			        try
			    	  {    		
			        		String connectivity_context = Context.WIFI_SERVICE;
			        		WifiManager wifi = (WifiManager)getSystemService(connectivity_context);
			        		//WifiInfo text_estatic = wifi.getConnectionInfo();
			        		String text="";
			        		wifi.startScan();
			        		results= wifi.getScanResults();
			        		Calendar c = Calendar.getInstance(); 
			        		scanResults=String.valueOf(c.get(Calendar.HOUR))+":"+String.valueOf(c.get(Calendar.MINUTE))+":"+String.valueOf(c.get(Calendar.SECOND))+":"+String.valueOf(c.get(Calendar.MILLISECOND))+"\n"; 
			    		}
			    		catch(Exception e)
			    		{
			    			Log.d("WIFISCAN", e.getMessage());
			    		}
		        	  try
					  {
		        		 
			        		for (ScanResult result : results)
			        		{
			        				
			        					
			        					SQLEjecutar = "select Location from A"+result.BSSID.replace(":", "")+" where Power>"+(result.level-5)+" AND POWER<"+(result.level+5);
			        					//SQLEjecutar ="select Power,Location from A"+result.BSSID.replace(":", "");
			        					conectarBDMySQL(usuarioMySQL, contrasenaMySQL, ipServidorMySQL, puertoMySQL, "prova");
			        					//Log.d("CONECTADO", "BONA CONEXIO");
			        					//conectarBDMySQL(usuarioMySQL, contrasenaMySQL, ipServidorMySQL, puertoMySQL, "");
			        					resultadoSQL="";
			        					resultadoSQL = ejecutarConsultaSQL(false, getApplication());
			        					//Log.d("consulta", "BONA Consulta");
						              	scanResults = scanResults+"SSID: "+result.SSID+" "+result.level+"\n"+resultadoSQL+"\n";
			        		}  
					  }
		    			catch (Exception e) 
		              {  
		            	  Log.d("SQL query", ""+e.getMessage());
		              }
		        	  
			        runOnUiThread(new Runnable() {
			          @Override
			          public void run() {
			            // update TextView here!
			        	  textView.setText(scanResults);
			          }
			        });
			        
			      }
			    } catch (InterruptedException e) {
			    	//Looper.loop();
			    }
			  }
			};

			t.start();
			/*
			Button start = (Button)findViewById(R.id.start_scan);
			start.setOnClickListener(new View.OnClickListener() 
	        {
		          public void onClick(View v) 
		          {
		        	  //Conectamos con el servidor de MySQL directamente
		              try
					  {
		            	  
		            	  String conexionMySQLURL = "jdbc:mysql://" + 
		            	  		ipServidorMySQL + ":" + puertoMySQL;
		            	  String usuario = "rroot";
		            	  String contrasena = "P@@ssw0rd";
		            	  
		            	  Toast.makeText(getApplicationContext(),
				                    "Conectando a servidor MySQL", 
				                    Toast.LENGTH_SHORT).show();
		            	  	
		            	  Class.forName("com.mysql.jdbc.Driver").newInstance();  
		            	  Connection con = DriverManager.getConnection(conexionMySQLURL, 
		            			  usuario, contrasena);
		            	  
		            	  Toast.makeText(getApplicationContext(),
				                    "Conectado Servidor MySQL", 
				                    Toast.LENGTH_LONG).show();
		            	  con.close();            	  
					  }
		              catch (ClassNotFoundException e) 
		              {
				      	  Toast.makeText(getApplicationContext(),
				                    "Error: " + e.getMessage(),
				                    Toast.LENGTH_SHORT).show();
		              } 
		              catch (SQLException e) 
		              {
				      	  Toast.makeText(getApplicationContext(),
				                    "Error: " + e.getMessage(),
				                    Toast.LENGTH_SHORT).show();
		              }
		              catch (Exception e) 
		              {  
		            	  Toast.makeText(getApplicationContext(),
				                    "Error: " + e.getMessage(),
				                    Toast.LENGTH_LONG).show();
		              }
		          }
		        });
		*/
		/*
		Thread t = new Thread() {

			  @Override
			  public void run() {
        Log.d("Trying to instantiate driver", "try");
        try {
                        Class.forName("com.mysql.jdbc.Driver").newInstance();

                        try {
							conexionMySQL =	DriverManager.getConnection("jdbc:mysql://"+ip+":"+port,usuario,contrasena);
							Log.d("CONNECTION", "connected");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.d("CONNECTION", "not connected: "+e.getMessage());
						}
                        
    	    			
                } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                	Log.d("com.mysql.jdbc.Driver", "NOT found");
                        e.printStackTrace();
                } catch (IllegalAccessException e) {
                	Log.d("com.mysql.jdbc.Driver", "ilegal access");
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (InstantiationException e) {
                	Log.d("com.mysql.jdbc.Driver", "Instantiation exception");
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
			  }
		};
		t.start();
		*/
		
	}
	
	protected void onResume()
	{
		super.onResume();
		
	    
	}
	    
    public void conectarBDMySQL (String usuario, String contrasena, 
    		String ip, String puerto, String catalogo)
    {
    	

    		String urlConexionMySQL = "";
    		if (catalogo != "")
    			urlConexionMySQL = "jdbc:mysql://" + ip + ":" +	
    			    puerto + "/" + catalogo;
    		else
    			urlConexionMySQL = "jdbc:mysql://" + ip + ":" + puerto;
    		if (usuario != "" & contrasena != "" & ip != "" & puerto != "")
    		{
    			
    			try 
    			{
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					//Log.d("Connection", urlConexionMySQL.toString()+":"+usuario+":"+contrasena);
	    			conexionMySQL =	DriverManager.getConnection(urlConexionMySQL, usuario, contrasena);
	    			//Log.d("Connection2", conexionMySQL.toString());		
				} 
    			catch (ClassNotFoundException e) 
    			{
    		      	  Toast.makeText(getApplicationContext(),
    		                    "Error: " + e.getMessage(),
    		                    Toast.LENGTH_SHORT).show();
    		      	Log.d("conetarBDMySQL not found", e.getMessage());
    			} 
    			catch (SQLException e) 
    			{
			      	  Toast.makeText(getApplicationContext(),
			                    "Error: " + e.getMessage(),
			                    Toast.LENGTH_SHORT).show();
			      	Log.d("conetarBDMySQL sql exeption", e.getMessage());
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    }
    
    public static String ejecutarConsultaSQL(Boolean SQLModificacion, Context context)
    {
    	
        try
		{
          String resultadoSQL = "";
          //ejecutamos consulta SQL de selección (devuelve datos)
      	  if (!SQLModificacion)
      	  {	            	    
      		  
      		  Statement st = conexionMySQL.createStatement();
      		  ResultSet rs = st.executeQuery(SQLEjecutar); 
      		  Log.d("SQL query", SQLEjecutar);

			  Integer numColumnas = 0;
				  
          	  //número de columnas (campos) de la consula SQL            	  
          	  numColumnas = rs.getMetaData().getColumnCount();          	  

      		  //obtenemos el título de las columnas
      		  for (int i = 1; i <= numColumnas; i++)
      		  {
              	  if (resultadoSQL != "")
              		  if (i < numColumnas)
              			  resultadoSQL = resultadoSQL + 
              			  		rs.getMetaData().getColumnName(i).toString() + ";";
              		  else
              			  resultadoSQL = resultadoSQL + 
              			  		rs.getMetaData().getColumnName(i).toString();
              	  else
              		  if (i < numColumnas)
              			  resultadoSQL = 
              				  rs.getMetaData().getColumnName(i).toString() + ";";
              		  else
              			  resultadoSQL = 
              				  rs.getMetaData().getColumnName(i).toString();                  	  
      		  }

          	  
          	  //mostramos el resultado de la consulta SQL
          	  while (rs.next()) 
          	  {  
          		  resultadoSQL = resultadoSQL + "\n";
          		  
          		  //obtenemos los datos de cada columna
          		  for (int i = 1; i <= numColumnas; i++)
                  {                          
                        if (rs.getObject(i) != null)
                        {
                      	  if (resultadoSQL != "")
                      		  if (i < numColumnas)
                      			  resultadoSQL = resultadoSQL + 
                      			  		rs.getObject(i).toString() + ";";
                      		  else
                      			  resultadoSQL = resultadoSQL + 
                      			  		rs.getObject(i).toString();
                      	  else
                      		  if (i < numColumnas)
                      			  resultadoSQL = rs.getObject(i).toString() + ";";
                      		  else
                      			  resultadoSQL = rs.getObject(i).toString();
                        }
                        else
                        {
                      	  if (resultadoSQL != "")
                      		  resultadoSQL = resultadoSQL + "null;";
                      	  else
                      		  resultadoSQL = "null;";
                        }                           
                    }
                    resultadoSQL = resultadoSQL + "\n";
          	  }
      		  st.close();
      		  rs.close();        		  
		  }
      	  // consulta SQL de modificación de 
      	  // datos (CREATE, DROP, INSERT, UPDATE)
      	  else 
      	  {
      		  int numAfectados = 0; 
      		  Statement st = conexionMySQL.createStatement();
      		  numAfectados = st.executeUpdate(SQLEjecutar);
      		  resultadoSQL = "Registros afectados: " + String.valueOf(numAfectados);
      		  st.close();
      	  }
      	  return resultadoSQL;
		}
        
        catch (Exception e) 
        {  
      	  Toast.makeText(context,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
      	Log.d("EjecutarBDMySQL", e.getMessage());
      	  return "";
        }
    }
    
    @Override
    public void onDestroy()
    {
      super.onDestroy();
    }  

}
