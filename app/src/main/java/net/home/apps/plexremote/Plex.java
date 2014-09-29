package net.home.apps.plexremote;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 9/22/14.
 */
public class Plex {
    Context context = null;
    ProgressDialog dialog;

    public Plex (Context ctx)
    {
        this.context = ctx;
    }

    public void GetPlexClients(String url)
    {
        //https://dl.dropboxusercontent.com/u/2152890/client.xml
        GetClientXML clients = new GetClientXML();
        clients.execute(url);
    }

    /**
     * Model for Server
     */
    public static class Server{
        public String name = null;
        public String ip = null;
        public String port = null;

        public void setName (String name)
        {
            this.name = name;
        }

        public void setIp (String ip)
        {
            this.ip = ip;
        }

        public void setPort (String port)
        {
            this.port = port;
        }
    }

    /**
     * Model for Client
     */
    public static class Client{
        public String name = null;
        public String ip = null;
        public String port = null;


        public void setName (String name)
        {
            this.name = name;
        }

        public void setIp (String ip)
        {
            this.ip = ip;
        }

        public void setPort (String port)
        {
            this.port = port;
        }
    }

    //TODO: Move this to it's own external class??
    /**
     * Get Clients
     * Get the clients from the server
     *
     *  9/22/14 - Current bug in PLEX GDM might cause this to fail. Need check method
     *  for each client found.
     */

    private class GetClientXML extends AsyncTask <String, Void, String[]>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Fetching Plex Clients", "Searching for Plex clients...");
        }

        @Override
        protected String[] doInBackground(String... clientFeed)
        {
            String[] clientNames = new String[10];

            try
            {
                URL url = new URL(clientFeed[0].toString());
                //URLConnection connection = url.openConnection();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(url.openConnection().getInputStream(),"UTF_8");

                // Returns the type of event: START_TAG, END_TAG, etc...
                int eventType = xpp.getEventType();

                int clientCount = 0;

                while(eventType != XmlPullParser.END_DOCUMENT)
                {
                    if(eventType == XmlPullParser.START_TAG)
                    {
                        if(xpp.getName().equalsIgnoreCase("server"))
                        {
                            clientNames[clientCount] = xpp.getAttributeValue(0);
                        }
                    }
                    eventType = xpp.next();
                }

            } catch(Exception ex)
            {
                // TODO Do something with the error
                Log.e("Plex Remote", ex.toString());
                ex.printStackTrace();
            }
            return clientNames;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String[] results)
        {
            // To get rid of the dialog
            dialog.dismiss();
            

        }
    }
}
