package com.example.lucascarvalho.adopet;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Lucas Carvalho on 07/11/2017.
 */

public class AnimalList extends AppCompatActivity {

    GridView gridView;
    ArrayList<Animal> list;
    AnimalListAdapter adapter = null;
    public final static String teste = "com.example.lucascarvalho.adopet.Id";


    private String getAnimal(int id) {
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM ANIMAIS WHERE ID =" + id);

        String name = "aaa";
        while (cursor.moveToNext()) {
            int idA = cursor.getInt(0);
            name = cursor.getString(1);
            String porte = cursor.getString(2);
            int idade = cursor.getInt(3);
            String dono = cursor.getString(4);
            String sexo = cursor.getString(5);
            byte[] image1 = cursor.getBlob(6);
            byte[] image2 = cursor.getBlob(7);
            byte[] image3 = cursor.getBlob(8);
            byte[] image4 = cursor.getBlob(9);
            String especie = cursor.getString(10);
            String localizacao = cursor.getString(11);
        }
        adapter.notifyDataSetChanged();
        return name;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animais_list_activity);

        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new AnimalListAdapter(this, R.layout.animal_items, list);
        gridView.setAdapter(adapter);

        //get todos dados do sqlite
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM ANIMAIS");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);

            String name = cursor.getString(1);
            String porte = cursor.getString(2);
            int idade = cursor.getInt(3);
            String dono = cursor.getString(4);
            String sexo = cursor.getString(5);
            byte[] image1 = cursor.getBlob(6);
            byte[] image2 = cursor.getBlob(7);
            byte[] image3 = cursor.getBlob(8);
            byte[] image4 = cursor.getBlob(9);
            String especie = cursor.getString(10);
            String localizacao = cursor.getString(11);

            list.add(new Animal(id, name, porte,idade, dono,sexo, image1, image2, image3, image4, especie, localizacao));
        }
        adapter.notifyDataSetChanged();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(AnimalList.this);
                dialog.setTitle("Escolha uma ação");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        if (item == 0) {
                            //update
                            Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM ANIMAIS");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            //dialog update
                            showDialogUpdate(AnimalList.this, arrID.get(position));
                        } else {
                            //delete
                            Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM ANIMAIS");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            ;
                            //dialog delete
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM ANIMAIS");
                ArrayList<Integer> arrID = new ArrayList<Integer>();
                while (c.moveToNext()) {
                    arrID.add(c.getInt(0));
                }
                ;

                Intent i = new Intent(AnimalList.this, AnimalView.class);

                i.putExtra("iNome", MainActivity.sqLiteHelper.selectData(arrID.get(position)).getNome());
                i.putExtra("iDescricao", MainActivity.sqLiteHelper.selectData(arrID.get(position)).getPorte());
                i.putExtra("idd", arrID.get(position));

                startActivity(i);
            }
        });
    }

    ImageView imageViewAnimal1;
    ImageView imageViewAnimal2;
    ImageView imageViewAnimal3;
    ImageView imageViewAnimal4;

    private void showDialogUpdate(Activity activity, final int position){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_animal_activity);
        dialog.setTitle("Update");

        imageViewAnimal1 = (ImageView) dialog.findViewById(R.id.imgv1);
        imageViewAnimal2 = (ImageView) dialog.findViewById(R.id.imgv2);
        imageViewAnimal3 = (ImageView) dialog.findViewById(R.id.imgv3);
        imageViewAnimal4 = (ImageView) dialog.findViewById(R.id.imgv4);
        final EditText edtNome = (EditText) dialog.findViewById(R.id.edtNome);
        final EditText edtPorte = (EditText) dialog.findViewById(R.id.edtEspecie);
        final EditText edtIdade = (EditText) dialog.findViewById(R.id.edtIdade);
        final EditText edtDono = (EditText) dialog.findViewById(R.id.edtDono);
        final EditText edtSexo = (EditText) dialog.findViewById(R.id.edtSexo);
        final EditText edtEspecie = (EditText) dialog.findViewById(R.id.edtEspecie);
        final EditText edtLocalizacao = (EditText) dialog.findViewById(R.id.edtLocalizacao);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnAtualizar);
        Button btnTeste = (Button) dialog.findViewById(R.id.btnTeste);
        //largura para o dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //altura para o dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.9);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        btnTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity.sqLiteHelper.updateData(
                            edtNome.getText().toString().trim(),
                            edtPorte.getText().toString().trim(),
                            Integer.parseInt(edtIdade.getText().toString().trim()),
                            edtDono.getText().toString().trim(),
                            edtSexo.getText().toString().trim(),
                            MainActivity.imageViewToByte(imageViewAnimal1),
                            MainActivity.imageViewToByte(imageViewAnimal2),
                            MainActivity.imageViewToByte(imageViewAnimal3),
                            MainActivity.imageViewToByte(imageViewAnimal4),
                            edtEspecie.getText().toString().trim(),
                            edtLocalizacao.getText().toString().trim(),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Atualizado com Sucesso", Toast.LENGTH_SHORT).show();
                }
                catch (Exception error){
                    Log.d("Update error: ", error.getMessage());
                }
                updateAnimalList();
            }
        });

        imageViewAnimal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //requere galeria de fotos
                ActivityCompat.requestPermissions(
                        AnimalList.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity.sqLiteHelper.updateData(
                            edtNome.getText().toString().trim(),
                            edtPorte.getText().toString().trim(),
                            Integer.parseInt(edtIdade.getText().toString().trim()),
                            edtDono.getText().toString().trim(),
                            edtSexo.getText().toString().trim(),
                            MainActivity.imageViewToByte(imageViewAnimal1),
                            MainActivity.imageViewToByte(imageViewAnimal2),
                            MainActivity.imageViewToByte(imageViewAnimal3),
                            MainActivity.imageViewToByte(imageViewAnimal4),
                            edtEspecie.getText().toString().trim(),
                            edtLocalizacao.getText().toString().trim(),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Atualizado com Sucesso", Toast.LENGTH_SHORT).show();
                }
                catch (Exception error){
                    Log.d("Update error: ", error.getMessage());
                }
                updateAnimalList();
            }
        });
    }

    private void showDialogDelete(final int idAnimal){
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(AnimalList.this);

        dialogDelete.setTitle("Cuidado!!");
        dialogDelete.setMessage("Tem certeza que deseja deletar?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    MainActivity.sqLiteHelper.deleteData(idAnimal);
                    Toast.makeText(getApplicationContext(), "Deletado com Sucesso", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.e("error",e.getMessage());
                }
                updateAnimalList();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void updateAnimalList(){
        //get todos dados do sqlite
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM ANIMAIS");
        list.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String porte = cursor.getString(2);
            int idade = cursor.getInt(3);
            String dono = cursor.getString(4);
            String sexo = cursor.getString(5);
            byte[] image1 = cursor.getBlob(6);
            byte[] image2 = cursor.getBlob(7);
            byte[] image3 = cursor.getBlob(8);
            byte[] image4 = cursor.getBlob(9);
            String especie = cursor.getString(10);
            String localizacao = cursor.getString(11);

            list.add(new Animal(id, name, porte, idade, dono,sexo,image1,image2,image3,image4,especie,localizacao));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 888){
            if(grantResults.length > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(),"Você não tem permissão para acessar o local do arquivo", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 888 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewAnimal1.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
