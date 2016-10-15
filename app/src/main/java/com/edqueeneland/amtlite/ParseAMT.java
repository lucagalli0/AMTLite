package com.edqueeneland.amtlite;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ParseAMT extends AsyncTask<String, Void, List<String>> {

    private static final int TIMEOUT = 7000;
    Context context;
    ProgressBar bar;

    public ParseAMT(Context context) {
        this.context = context;
        bar = new ProgressBar(context);
    }
    @Override
    protected void onPreExecute(){}

    @Override
    protected List<String> doInBackground(String... param) {

        List<String> lista = new ArrayList<>();
        try {
            Log.d("DBG" , "try: " + param[0]);
            Connection.Response response = Jsoup.connect("http://www.amt.genova.it/amt/servizi/passaggi_tel.php")
                    .timeout(TIMEOUT)
                    .method(Connection.Method.POST)
                    .data("CodiceFermata", param[0])
                    .data("conferma", "Conferma")
                    .execute();

            String nBus = String.format(Locale.ITALY, "%03d", Integer.valueOf(param[1]));
            Elements orario;
            orario = response.parse().select(":contains(" + nBus + ") + td +td +td");
            Log.d("DBG", "orario: " + orario.toString());
            if (orario.hasText())
                lista = new ArrayList<>(Arrays.asList(orario.text().split(" ")));
            else
                lista.add("Nessun bus con quel numero in arrivo :(");
        } catch (IOException e) {
            lista.add("Non sono riuscito a connettermi :(");
        }
        Log.d("DBG", "run: " + lista.toString());
        return lista;
    }

    @Override
    protected void onPostExecute(List<String> result) {

        String classe = context.getClass().getSimpleName();
        if (classe.equals("Insert"))
            Insert.bar.setVisibility(View.INVISIBLE);
        else
            StopActivity.bar.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(context, Results.class);
        String[] res = result.toArray(new String[result.size()]);
        intent.putExtra("RIS", res);
        context.startActivity(intent);

    }
}