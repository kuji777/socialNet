package socialg.com.vyz.socialgaming.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import socialg.com.vyz.socialgaming.NewsDisplayActivity;
import socialg.com.vyz.socialgaming.R;
import socialg.com.vyz.socialgaming.bean.News;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout newsContainer;

    private OnFragmentInteractionListener mListener;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        //Friend test
        News news= new News();
        news.setTitle("Une pincée d'infos pour Yo-Kai Watch 4");
        news.setContent("Malgré un évident essoufflement après l'indécent succès du deuxième épisode, Yo-Kai Watch reste encore le poids lourd de Level-5 : Yo-Kai Watch 3, ça reste environ 2,5 millions de ventes au Japon (toutes versions confondues) et le spin-off Blasters 2 a récemment tapé le demi-million.\n" +
                "\n" +
                "Pour cela qu'il va falloir surveiller les effets du futur Yo-Kai Watch 4 pour la Switch, même si le titre risque finalement de rater les fêtes, la sortie passant de « 2018 » à « cet hiver ».\n" +
                "\n" +
                "Le magazine CoroCoro apporte quelques indications en déclarant que cet épisode jouera cette fois la carte des voyages dans le temps et entre les univers, expliquant un peu le premier teaser diffusé il y a peu (voir ci-dessous) et selon les sources de Gematsu, le rendu devrait être encore meilleur que ce qu'aurait dû être la licence avant d'être transposée sur 3DS.");

        newsContainer = view.findViewById(R.id.news_container);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final LinearLayout childView = (LinearLayout) layoutInflater.inflate(R.layout.single_news, null);
        ((TextView) childView.findViewById(R.id.news_title)).setText(news.getTitle());
        ((TextView) childView.findViewById(R.id.news_content)).setText(news.getContent());

        final int id = news.getId();
        childView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsContainer.removeView(v);
                Intent intent = new Intent(childView.getContext(),NewsDisplayActivity.class);
                intent.putExtra("id",id);
                Log.i("TESTID","signal : n°"+id);
                startActivity(intent);
            }
        });
        newsContainer.addView(childView);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
