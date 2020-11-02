package corelibrary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class LocationHelper
{
	public static void Init()
	{
		ArrayList<Lokasi> x = getDataLokasi();
	}

	private static ArrayList<Lokasi> _lokasi;

	public static ArrayList<Lokasi> getDataLokasi()
	{
		if (_lokasi == null)
		{
			String json = Resources.GetResources("Lokasi");
			Gson gsonBuilder = new GsonBuilder().create();
			Lokasi[] hasil = gsonBuilder.fromJson(json, Lokasi[].class);
			_lokasi = new ArrayList<Lokasi>();
			Collections.addAll(_lokasi, hasil);

		}
		return _lokasi;
	}


	public static Iterable<String> GetPropinsi()
	{
		HashSet<String> Propinsis = new HashSet<String>();
		for (Lokasi item : getDataLokasi())
		{
			if (!Propinsis.contains(item.getPropinsi()))
			{
				Propinsis.add(item.getPropinsi());
			}
		}
		//var data = DataLokasi.Select(x => x.Propinsi).Distinct();
		List<String> list = new ArrayList<String>(Propinsis);
		return list;
	}

	public static Iterable<String> GetKabupaten()
	{
		return GetKabupaten(null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static IEnumerable<string> GetKabupaten(string Propinsi=null)
	public static Iterable<String> GetKabupaten(String Propinsi)
	{
		if (tangible.StringHelper.isNullOrEmpty(Propinsi))
		{
			ArrayList<String> data = new ArrayList<String>();
			for (Lokasi item : getDataLokasi())
			{
				data.add(item.getKabupaten());
			}
			//DataLokasi.Select(x => x.Kabupaten);
			return data;
		}
		else
		{
			/*
			var data = from x in DataLokasi
			           where x.Propinsi == Propinsi
			           select x;*/
			HashSet<String> data = new HashSet<String>();
			for (Lokasi item : getDataLokasi())
			{
				if (item.getPropinsi().equals(Propinsi) && !data.contains(item.getKabupaten()))
				{
					data.add(item.getKabupaten());
				}
			}
			List<String> list = new ArrayList<String>(data);
			return list;
			/*
			if(data!=null && data.Count()>0)
			    return data.Select(x => x.Kabupaten).Distinct();
			*/
		}
		//return null;
	}
}