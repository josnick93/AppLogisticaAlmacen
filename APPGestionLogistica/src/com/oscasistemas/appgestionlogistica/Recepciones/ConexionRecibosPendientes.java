package com.oscasistemas.appgestionlogistica.Recepciones;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.oscasistemas.appgestionlogistica.articulo.SendInfo;
import com.oscasistemas.appgestionlogistica.utils.SendInfoAdapter;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ConexionRecibosPendientes extends AsyncTask<String, Void, String> {

	private ListView recibosListView;
	private ArrayList<SendInfo> pedidos;
	private Activity activity;
	private int nOrder,position;

	public ConexionRecibosPendientes(Activity act, ListView recibosListView,int nOrder,int position) {
		// TODO Auto-generated constructor stub
		this.recibosListView = recibosListView;
		this.activity = act;
		this.pedidos=new ArrayList<SendInfo>();
		this.nOrder=nOrder;
		this.position=position;
	}

	@Override
	protected String doInBackground(String... urls) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.2.2:3306/erp_osca", "usuario",
					"osca-SiStEmAs");

			Statement st = null;
			ResultSet rs = null;

			try {
				/**
				 * Info Articulo
				 */
				String QSLEjecutar = query();

				st = conn.createStatement();
				st.addBatch("USE erp_osca");
				rs = st.executeQuery(QSLEjecutar);
				pedidos.clear();
				while (rs.next()) {
					pedidos.add(new SendInfo(rs.getString("p.pedido"), rs.getString("c.nombre"),
							rs.getString("p.Referencia"), rs.getDate("p.Fecha"), rs.getDate("p.FechaEntrega")));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				conn.close();
				rs.close();
				st.close();
				return "";
			} finally {
				rs.close();
				st.close();
				conn.close();
			}
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
		}
		return "";

	}

	private String query() {
		if (nOrder != -1)
			return "Select c.nombre ,p.* From ppedido p,proveedor c WHERE p.Proveedor=c.Proveedor ";
		switch (position) {
		// Fecha descendente
		case 0:
			return "Select c.nombre ,p.* From ppedido p,proveedor c WHERE p.Proveedor=c.Proveedor  ORDER BY p.Fecha  DESC LIMIT 50";
		// Fecha ascendente
		case 1:
			return "Select c.nombre ,p.* From ppedido p,proveedor c WHERE p.Proveedor=c.Proveedor  ORDER BY ORDER BY p.Fecha ASC LIMIT 50 ";
		// Cliente Ascendente
		case 2:
			return "Select c.nombre ,p.* From ppedido p,proveedor c WHERE p.Proveedor=c.Proveedor  ORDER BY c.Nombre DESC LIMIT 50 ";
		// Cliente Descendente
		case 3:
			return "Select c.nombre ,p.* From ppedido p,proveedor c WHERE p.Proveedor=c.Proveedor  ORDER BY c.Nombre ASC LIMIT 50 ";
		// Identificador ascendente
		case 4:
			return "Select c.nombre ,p.* From ppedido p,proveedor c WHERE p.Proveedor=c.Proveedor  ORDER BY p.Pedido DESC LIMIT 50 ";
		// Identificador descendente
		case 5:
			return "Select c.nombre ,p.* From ppedido p,proveedor c WHERE p.Proveedor=c.Proveedor  ORDER BY p.Pedido ASC LIMIT 50 ";

		default:
			return "Select c.nombre ,p.* From palbaran p,proveedor c WHERE p.Proveedor=c.Proveedor   ORDER BY p.Fecha  DESC LIMIT 50";
		}
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
//		SendInfoAdapter inf = new SendInfoAdapter(this.activity, pedidos);
//		recibosListView.setAdapter(inf);
//		inf.notifyDataSetChanged();
	}


}
