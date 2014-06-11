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

	String database="prova";
	static Connection conexionMySQL;
	static String SQLEjecutar;
	String resultadoSQL;
	//String[][][] sourceElements=new String[3][3][3];
	boolean activityStopped = false;
	int numFilas;
	int numColumnas;
	int numScans=0;
	String SQLTablas;
	
	/**
	 * El método onCreate se ejecuta al crear la aplicación
	 * @param Bundle savedInstance Usado para pasar datos a través de diferentes Activities
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.hello_world);
		textView.setText("Redes No escaneadas");
		//este thread hará el escaneo de las redes y las queries a la base de datos.
		Thread t = new Thread() {

			  @Override
			  public void run() {
			    try {
			    	Looper.prepare();
			    	Log.d("activityStopped ",String.valueOf(activityStopped));
			    
			      while (!activityStopped) {
			    //while (!isInterrupted()) {  
			        
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
		        		  	SQLTablas="";
			        		for (ScanResult result : results)
			        		{
			        				
			        					//Log.d("consulta", "size: "+results.size()+" i:"+i);
			        			//if(result.level>-60)
			        			//{
			        				//SQLTablas = SQLTablas+"CREATE TABLE IF NOT EXISTS A"+result.BSSID.replace(":", "")+" LIKE A62233d6ee648";
		        					//SQLEjecutar = SQLEjecutar+"SELECT '"+result.BSSID.replace(":", "")+"' as tableName, Location, Floor from A"+result.BSSID.replace(":", "")+" where Power>"+(result.level-5)+" AND POWER<"+(result.level+5);
		        					//SQLEjecutar = SQLEjecutar+" IF (SELECT A"+result.BSSID.replace(":", "")+" FROM information_schema.tables WHERE table_schema='"+database+"' AND table_name='A"+result.BSSID.replace(":", "")+") THEN select 'A"+result.BSSID.replace(":", "")+"' as tableName, Location, Floor from A"+result.BSSID.replace(":", "")+" where Power>"+(result.level-5)+" AND POWER<"+(result.level+5)+" END IF";
			        			if(result.level>-60)
			        			{	
			        				SQLEjecutar=SQLEjecutar+"select '"+result.BSSID.replace(":", "")+"' as tableName, Location, Floor from A"+result.BSSID.replace(":", "")+" where Power>"+(result.level-8)+" AND POWER<"+(result.level+8);
			        			}
			        			else
			        			{
			        				SQLEjecutar=SQLEjecutar+"select '"+result.BSSID.replace(":", "")+"' as tableName, Location, Floor from A"+result.BSSID.replace(":", "")+" where Power>"+(result.level-5)+" AND POWER<"+(result.level+5);

			        			}
			        			
			        			//}
			        			/*
			        			else
			        			{
		        					SQLEjecutar = SQLEjecutar+"select '"+result.BSSID.replace(":", "")+"' as tableName, Location, Floor from A"+result.BSSID.replace(":", "")+" where Power>"+(result.level-5)+" AND POWER<"+(result.level+5);
			        			}
			        			*/
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
			        						//SQLTablas=SQLTablas+" UNION ";
			        					}
			        					++i;
			        		}  
			        		
			        		conectarBDMySQL(usuarioMySQL, contrasenaMySQL, ipServidorMySQL, puertoMySQL, database);
        					//Log.d("CONECTADO", "BONA CONEXIO");
        					//conectarBDMySQL(usuarioMySQL, contrasenaMySQL, ipServidorMySQL, puertoMySQL, "");
        					//resultadoSQL="";
			        		//resultadoSQL = ejecutarConsultaSQL(false, getApplication());
			        		//ejecutarConsultaCambios(null, getApplicationContext());
        					String [][] array =consultaSQLArray(false, getApplicationContext());
        					
        					scanResults =numScans+"- "+getLocation(array);
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
			        Thread.sleep(10000);
			        numScans=numScans+1;
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
          //ejecutamos consulta SQL de selecciÃ³n (devuelve datos)
      	  if (!SQLModificacion)
      	  {	            	    
      		  
      		  Statement st = conexionMySQL.createStatement();
      		  ResultSet rs = st.executeQuery(SQLEjecutar); 
      		  Log.d("SQL query", SQLEjecutar);

			  Integer numColumnas = 0;
				  
          	  //nÃºmero de columnas (campos) de la consula SQL            	  
          	  numColumnas = rs.getMetaData().getColumnCount();          	  
          	Log.d("culumnas", numColumnas.toString());
      		  //obtenemos el tÃ­tulo de las columnas
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
      	  // consulta SQL de modificaciÃ³n de 
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
    
    public void ejecutarConsultaCambios(Boolean SQLModificacion, Context context)
    {
    	
        try
		{

          //ejecutamos consulta SQL de selecciÃ³n (devuelve datos)
    

      		  Statement st = conexionMySQL.createStatement();
      		  st.executeUpdate(SQLTablas);
      		  st.close();
		}
        
        catch (Exception e) 
        {  
      	  Toast.makeText(context,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
      	Log.d("EjecutarBDMySQL", e.getMessage());
        }
    }
    
    public String[][] consultaSQLArray(Boolean SQLModificacion, Context context)
    {
    	String[][] sourceElements=new String [0][0];
        try
		{
          String resultadoSQL = "";
          //ejecutamos consulta SQL de selecciÃ³n (devuelve datos)
      	  if (!SQLModificacion)
      	  {	            	    
      		  
      		  Statement st = conexionMySQL.createStatement();
      		  ResultSet rs = st.executeQuery(SQLEjecutar); 
      		  //Log.d("SQL query", SQLEjecutar);

			  numColumnas = 0;
			  numFilas = 0;
          	  //nÃºmero de columnas (campos) de la consula SQL            	  
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
      	  // consulta SQL de modificaciÃ³n de 
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
    
	/**
	 * Esta función contiene el algoritmo usado para decidir la localización.
	 * @param String[][] array. Este array contiene la query a la base de datos. LA primera dimensión son 
	 * las filas de la query, la columna [i][0], es el nombre del access point, la columna [i][1] es la
	 * localización y la columna [i][2] es la planta en que se encuentra.
	 * @return String Location. La localización deducida
	 */
	public String getLocation( String[][] array)
	{
		String Location="";
		String[][] locationArray = new String [array.length][array.length];
		int k = 0;
		Log.d("longitud array", String.valueOf(array.length));
		for(int i=0; i<array.length; i++)
		{
			Boolean equal=false;
			for(int j=0; j<locationArray.length; j++)
			{
				if(array[i][1].equals(locationArray[j][0]) && array[i][2].equals(locationArray[j][3]))
				{
					equal=true;
		    		if(!array[i][0].equals(locationArray[j][1]))
		    		{
		    			Log.d("igual?", array[i][0]+" no es igual a "+locationArray[j][1]+" para el wifi "+locationArray[j][0]);
		    			locationArray[j][1]=array[i][0];
		    			locationArray[j][2]=String.valueOf(Integer.valueOf(locationArray[j][2])+1);
		    		}
				}
			}
			if(equal==false)
			{
				locationArray[k][0]=array[i][1];
				//almaceno el último wifi de la localización y en caso que sea distinto, se suman las repeticiones
				locationArray[k][1]=array[i][0];
				locationArray[k][2]="1";
				locationArray[k][3]=array[i][2];
				k++;
			}
		}
		int masApariciones=0;
		for (int j=0; j<=k-1; j++)
		{
			Log.d("Location: ", locationArray[j][0]);
			Log.d("ultimo wifi", locationArray[j][1]);
			Log.d("repeticiones:", locationArray[j][2]);
			Log.d("Planta:", locationArray[j][3]);
			if(Integer.valueOf(locationArray[j][2])>masApariciones)
			{
				masApariciones=Integer.valueOf(locationArray[j][2]);
				Location=" Estas a la planta "+locationArray[j][3]+" en el lugar "+locationArray[j][0];
			}
			else if(Integer.valueOf(locationArray[j][2])==masApariciones)
			{
				masApariciones=Integer.valueOf(locationArray[j][2]);
				Location=Location+" - Estas a la planta "+locationArray[j][3]+" en el lugar "+locationArray[j][0];
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
