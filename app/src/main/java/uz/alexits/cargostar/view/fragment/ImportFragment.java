package uz.alexits.cargostar.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.WorkInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.entities.transportation.Import;
import uz.alexits.cargostar.view.adapter.ImportAdapter;
import uz.alexits.cargostar.view.callback.ImportCallback;
import uz.alexits.cargostar.viewmodel.ImportViewModel;
import uz.alexits.cargostar.viewmodel.factory.ImportViewModelFactory;

public class ImportFragment extends Fragment implements ImportCallback {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImportAdapter importAdapter;

    private ImportViewModel importViewModel;

    public ImportFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ImportViewModelFactory importFactory = new ImportViewModelFactory(requireContext());
        importViewModel = new ViewModelProvider(getViewModelStore(), importFactory).get(ImportViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_import, container, false);

        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        final RecyclerView importListRecyclerView = root.findViewById(R.id.delivery_list_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        importListRecyclerView.setLayoutManager(layoutManager);
        importAdapter = new ImportAdapter(requireContext(), this);
        importListRecyclerView.setAdapter(importAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            importViewModel.fetchImportList();
        });
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        importViewModel.getImportList().observe(getViewLifecycleOwner(), importList -> {
            importAdapter.setImportList(importList);
        });

        importViewModel.getFetchImportListResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED || workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            swipeRefreshLayout.setRefreshing(true);
        });
    }

    @Override
    public void onImportItemClicked(int position, Import item, ImportCallback callback) {
        NavHostFragment.findNavController(this).navigate(ImportFragmentDirections.actionImportFragmentToInvoiceFragment());
    }
}