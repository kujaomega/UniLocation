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
	String scanResults;

	String database="uni";
	static Connection conexionMySQL;
	static String SQLEjecutar;
	String resultadoSQL;
	//String[][][] sourceElements=new String[3][3][3];
	boolean activityStopped = false;
	int numFilas;
	int numColumnas;
	
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
			    	Log.d("activityStopped ",String.valueOf(activityStopped));
			      while (!activityStopped) {
			    //while (!isInterrupted()) {  
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
		        		  	int i=0;
		        		  	SQLEjecutar="";
			        		for (ScanResult result : results)
			        		{
			        				
			        					//Log.d("consulta", "size: "+results.size()+" i:"+i);
			        			if(result.level>5)
			        			{
		        					SQLEjecutar = SQLEjecutar+"select '"+result.BSSID.replace(":", "")+"' as tableName, Location, Floor from A"+result.BSSID.replace(":", "")+" where Power>"+(result.level-5)+" AND POWER<"+(result.level+5);

			        			}
			        			else
			        			{
		        					SQLEjecutar = SQLEjecutar+"select '"+result.BSSID.replace(":", "")+"' as tableName, Location, Floor from A"+result.BSSID.replace(":", "")+" where Power>"+(result.level-5)+" AND POWER<"+(result.level+5);
			        			}
			        					//SQLEjecutar ="select Power,Location from A"+result.BSSID.replace(":", "");
			        					//conectarBDMySQL(usuarioMySQL, contrasenaMySQL, ipServidorMySQL, puertoMySQL, "prova");
			        					//Log.d("CONECTADO", "BONA CONEXIO");
			        					//conectarBDMySQL(usuarioMySQL, contrasenaMySQL, ipServidorMySQL, puertoMySQL, "");
			        					//resultadoSQL="";
			        					//resultadoSQL = ejecutarConsultaSQL(false, getApplication());
			        					//Log.d("consulta", "BONA Consulta");
						              	//scanResults = scanResults+"SSID: "+result.SSID+" "+result.level+"\n"+resultadoSQL+"\n";
			        					if( i != results.size()-1)
			        					{
			        						SQLEjecutar=SQLEjecutar+" UNION ";
			        					}
			        					++i;
			        		}  
			        		
			        		conectarBDMySQL(usuarioMySQL, contrasenaMySQL, ipServidorMySQL, puertoMySQL, database);
        					//Log.d("CONECTADO", "BONA CONEXIO");
        					//conectarBDMySQL(usuarioMySQL, contrasenaMySQL, ipServidorMySQL, puertoMySQL, "");
        					//resultadoSQL="";
			        		//resultadoSQL = ejecutarConsultaSQL(false, getApplication());
        					String [][] array =consultaSQLArray(false, getApplicationContext());
        					
        					scanResults =getLocation(array);
        					//Log.d("RESULT", String.valueOf(array));
			        		//Log.d("consulta", SQLEjecutar);
			              	//scanResults = resultadoSQL+"\n";
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
			    	Looper.loop();
			    }
			  }
			};

			t.start();

		
	}
	
	protected void onResume()
	{
		super.onResume();
		activityStopped = false;
		
	    
	}
	
	protected void onPause()
	{
		super.onPause();
		activityStopped = true;
		
		
	    
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
          	Log.d("culumnas", numColumnas.toString());
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
    
    public String[][] consultaSQLArray(Boolean SQLModificacion, Context context)
    {
    	String[][] sourceElements=new String [0][0];
        try
		{
          String resultadoSQL = "";
          //ejecutamos consulta SQL de selección (devuelve datos)
      	  if (!SQLModificacion)
      	  {	            	    
      		  
      		  Statement st = conexionMySQL.createStatement();
      		  ResultSet rs = st.executeQuery(SQLEjecutar); 
      		  //Log.d("SQL query", SQLEjecutar);

			  numColumnas = 0;
			  numFilas = 0;
          	  //número de columnas (campos) de la consula SQL            	  
          	  numColumnas = rs.getMetaData().getColumnCount();          	  
          	  rs.last();
          	  numFilas=rs.getRow();
          	  rs.first();
          	  sourceElements=new String[numFilas-1][numColumnas];
          	  Log.d("ENTRA", "columnas: "+numColumnas+" object:"+numFilas);

          	  //mostramos el resultado de la consulta SQL
          	  
          	  while (rs.next()) 
          	  {  
          		  resultadoSQL = resultadoSQL + "\n";
          		  
          		  //obtenemos los datos de cada columna
          		  for (int i = 1; i <= numColumnas; i++)
                  {       
          			sourceElements[rs.getRow()-2][i-1]=rs.getObject(i).toString();
              		//Log.d("ELEMENT", " ["+(rs.getRow()-2)+"]["+(i-1)+"]: "+sourceElements[rs.getRow()-2][i-1]);
          			  /*
                        if (rs.getObject(i) != null)
                        {

                      		sourceElements[rs.getRow()-2][i-1]=rs.getObject(i).toString();
                      		Log.d("ELEMENT", " ["+(rs.getRow()-2)+"]["+(i-1)+"]: "+sourceElements[rs.getRow()-1][i-1]);

                        }
                        else
                        {
                      		sourceElements[rs.getRow()-2][i-1]="null;";
                      		Log.d("ELEMENT", " ["+(rs.getRow()-2)+"]["+(i-1)+"]: "+sourceElements[rs.getRow()-1][i-1]);
                        }  
                        */                         
                    }
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
      	  return sourceElements;
		}
        /*
        catch(SQLException e)
        {
        	Log.d("EjecutarBDMySQL", e.getMessage());
        	return sourceElements;
        	 
        }
        */
        catch (Exception e) 
        {  
        	Log.d("EjecutarBDMySQL", e.getMessage());
        	return sourceElements;
        }
    }
    
    public String getLocation( String[][] array)
    {
    	String Location="";
    	String[][] locationArray = new String [array.length][array.length];
    	int k = 0;
    	Log.d("longitud array", String.valueOf(array.length));
    	for(int i=0; i<array.length; i++)
    	{
    		Boolean equal=false;
    		
    		/*
    		for (int j=0; j<array[i].length; j++)
    		{
    			Log.d("columnes", String.valueOf(array[i].length));
    			Log.d("POSICION", array[i][j]);
    		}
    		*/
    		for(int j=0; j<locationArray.length; j++)
    		{
    			if(array[i][1].equals(locationArray[j][0]))
    			{
    				equal=true;
    	    		if(!array[i][0].equals(locationArray[j][1]))
    	    		{
    	    			Log.d("igual?", array[i][0]+" no es igual a "+locationArray[j][1]+" per "+locationArray[j][0]);
    	    			locationArray[j][1]=array[i][0];
    	    			locationArray[j][2]=String.valueOf(Integer.valueOf(locationArray[j][2])+1);
    	    		}
    			}
    		}
    		if(equal==false)
    		{
    			
    			locationArray[k][0]=array[i][1];
    			locationArray[k][1]=array[i][0];
    			locationArray[k][2]="1";
    			locationArray[k][3]=array[i][2];
    			k++;
    		}
    		Log.d("WIFI", array[i][0]);

    	}
    	int masApariciones=0;
		for (int j=0; j<=k-1; j++)
		{
			Log.d("Location: ", locationArray[j][0]);
			Log.d("ultim wifi", locationArray[j][1]);
			Log.d("repeticions:", locationArray[j][2]);
			if(Integer.valueOf(locationArray[j][2])>masApariciones)
			{
				masApariciones=Integer.valueOf(locationArray[j][2]);
				Location=" Estas a la planta "+locationArray[j][3]+" al lloc "+locationArray[j][0];
			}
			
		}
		return Location;
    }
    
    @Override
    public void onDestroy()
    {
      super.onDestroy();
    }  

}
