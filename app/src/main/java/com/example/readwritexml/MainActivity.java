package com.example.readwritexml;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.shape.InterpolateOnScrollPositionChangeHelper;

import org.parceler.Parcels;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btn_save, btn_read;
    EditText e_prenom, e_nom;
    String filename = "userData.xml";
    XmlSerializer serializer = Xml.newSerializer();
    FileOutputStream fos;
    TextView tv_result;
    ArrayList<Person> people = new ArrayList<>();
    Person current_person = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_save = findViewById(R.id.btn_save);
        btn_read = findViewById(R.id.btn_read);
        e_prenom = findViewById(R.id.e_prenom);
        e_nom = findViewById(R.id.e_nom);
        e_prenom.setSelection(e_prenom.getText().length());
        e_nom.setSelection(e_nom.getText().length());
        tv_result = findViewById(R.id.tv_result);





        try {

            fos = openFileOutput(filename, Context.MODE_PRIVATE);
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "root");
        }
        catch (IOException e)
        {
            // Exception
        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WriteToXML(filename);
            }
        });
        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StopWriting();

                try {
                    XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                    parserFactory.setNamespaceAware(false);
                    XmlPullParser parser = parserFactory.newPullParser();
                    InputStream is = getAssets().open("data.xml");
                    parser.setInput(is,"UTF_8");

                    int evenType = parser.getEventType();
                    while (evenType != XmlPullParser.END_DOCUMENT)
                    {
                        switch (evenType)
                        {
                            case XmlPullParser.START_TAG:
                                if(parser.getName().equalsIgnoreCase("userData"))
                                {
                                    current_person = new Person();
                                    people.add(current_person);
                                }else if(parser.getName().equalsIgnoreCase("first_name"))
                                {
                                    current_person.first_name = parser.nextText();
                                }else if(parser.getName().equalsIgnoreCase("last_name"))
                                {
                                    current_person.last_name = parser.nextText();
                                }

                               break;
                        }
                        evenType = parser.next();
                    }

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Intent in = new Intent(MainActivity.this,ReadXMLfile.class);

                for(int i=0;i<people.size();i++)
                {
                    Parcelable parc = Parcels.wrap(people.get(i));
                    String str  = String.valueOf(i);
                    in.putExtra("people"+str,parc);
                }
                in.putExtra("size",String.valueOf(people.size()));
                startActivity(in);

               //printPeople(people);

            }

        });

    }
    public void printPeople(ArrayList<Person> people)
    {
        StringBuilder builder = new StringBuilder();
        for(Person p : people)
        {
            builder.append(p.first_name).append(" ").append(p.last_name).append("\n");
        }
        tv_result.setText(builder);
    }

    public void StopWriting(){
        try {
            serializer.endDocument();
            serializer.flush();
        }catch (IOException e){
            // exception
        }

    }
    public void WriteToXML(String filename)
    {
        try {
            if(e_prenom.getText().toString().isEmpty() || e_nom.getText().toString().isEmpty() )
            {
                Toast.makeText(this, "Enter the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            fos = openFileOutput(filename, Context.MODE_APPEND);
            StringWriter writer = new StringWriter();
            serializer.startTag(null, "UserData");
            serializer.startTag(null, "first_name");
            serializer.text(e_prenom.getText().toString());

            serializer.endTag(null, "first_name");
            serializer.startTag(null, "last_name");
            serializer.text(e_nom.getText().toString());
            serializer.endTag(null, "last_name");
            serializer.endTag(null,"UserData");
            serializer.flush();
            String dataWrite = writer.toString();
            fos.write(dataWrite.getBytes());
            fos.close();
            e_nom.setText("");
            e_prenom.setText("");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}