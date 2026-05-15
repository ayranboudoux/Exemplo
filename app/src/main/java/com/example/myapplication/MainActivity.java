package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView txtOuro, txtProduzindo;
    private Spinner spinner;
    private ProgressBar progress;
    private RecyclerView recycler;

    private Queue<ItemForja> fila;
    private ArrayList<ItemForja> lista;
    private AdapterFila adapter;
    private ItemForja[] itens;
    private int ouro = 100;
    private int tempoAtual = 0;
    private boolean produzindo = false;
    private ItemForja pronto = null;
    private Handler handler = new Handler();
    private Runnable timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtOuro = findViewById(R.id.txtOuro);
        txtProduzindo = findViewById(R.id.txtProduzindo);
        spinner = findViewById(R.id.spinner);
        progress = findViewById(R.id.progres);
        recycler = findViewById(R.id.recycler);
        criarItens();
        configurarSpinner();
        fila = new LinkedList<>();
        lista = new ArrayList<>();
        adapter = new AdapterFila(lista);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        iniciarTimer();
        atualizarTela();
    }

    private void iniciarTimer() {
        timer = new Runnable() {
            @Override
            public void run() {
                if(produzindo && pronto == null && !fila.isEmpty()){
                    ItemForja atual = fila.peek();
                    tempoAtual++;
                    progress.setMax(atual.getTempo());
                    progress.setProgress(tempoAtual);
                    txtProduzindo.setText("Produzindo: " + atual.getNome());
                    if(tempoAtual >= atual.getTempo()){
                        pronto = fila.peek();
                        produzindo = false;
                        tempoAtual = 0;
                        atualizarTela();
                    }
                }

                handler.postDelayed(this, 1000);
            }
        };
        handler.post(timer);
    }

    public void minerar(View v){
        int ganho = new Random().nextInt(50) + 10;
        ouro += ganho;
        Toast.makeText(this, "+"+ganho+" ouro", Toast.LENGTH_SHORT).show();;
        atualizarTela();
    }

    public void adicionarFila(View v){
        ItemForja item = (ItemForja) spinner.getSelectedItem();
        if(ouro < item.getCusto()){
            Toast.makeText(this, "Ouro insuficiente", Toast.LENGTH_SHORT).show();
            return;
        }
        ouro -= item.getCusto();
        fila.add(item);
        atualizarFila();
        atualizarTela();
    }

    public void iniciarProducao(View v){
        if(fila.isEmpty()){
            Toast.makeText(this, "Fila vazia", Toast.LENGTH_SHORT).show();
            return;
        }
        if(produzindo){
            Toast.makeText(this, "Ja produzindo", Toast.LENGTH_SHORT).show();
            return;
        }
        tempoAtual = 0;
        produzindo = true;
        atualizarTela();
    }

    public void venderItem(View v){
        if(pronto == null){
            Toast.makeText(this, "Nada pronto", Toast.LENGTH_SHORT).show();
            return;
        }
        ouro += pronto.getValorVenda();
        Toast.makeText(this, pronto.getNome() + "vendido por " + pronto.getValorVenda() + " ouro", Toast.LENGTH_SHORT).show();
        fila.poll();
        pronto = null;
        atualizarFila();
        atualizarTela();

    }

    private void atualizarFila() {
        lista.clear();
        lista.addAll(fila);
        adapter.notifyDataSetChanged();
    }

    private void atualizarTela() {
        txtOuro.setText("Ouro: " + ouro);
        if(pronto != null){
            txtProduzindo.setText("Pronto: " + pronto.getNome());
            progress.setProgress(0);
        }else if(produzindo){
            ItemForja atual = fila.peek();
            txtProduzindo.setText("Produzindo: " + atual.getNome());

        }else{
            txtProduzindo.setText("Parado");
            progress.setProgress(0);
        }
    }

    private void configurarSpinner() {
        ArrayAdapter<ItemForja> adapterSpinner = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, itens);
        spinner.setAdapter(adapterSpinner);
    }

    public void criarItens(){
        itens = new ItemForja[]{
                new ItemForja("Espada", 100, 8, 150),
                new ItemForja("Escudo", 120, 10, 180),
                new ItemForja("Armadura", 200, 15, 300),
                new ItemForja("Cimitarra", 70, 6, 120)
        };
    }
}