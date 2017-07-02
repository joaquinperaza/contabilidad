package app.peraza.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import app.peraza.domain.model.Category;
import app.peraza.mvp.presenter.CategoryDetailsPresenter;
import app.peraza.mvp.view.CategoryDetailsView;
import app.peraza.utils.IconUtils;
import app.peraza.view.adapter.IconsGridAdapter;
import app.peraza.view.alert.Alert;
import app.peraza.view.fragment.base.BaseMvpFragment;
import app.peraza.view.helper.TextWatcherAdapter;

import java.util.Random;

import javax.inject.Inject;

import butterknife.Bind;
import uz.shift.colorpicker.LineColorPicker;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class CategoryDetailsFragment extends BaseMvpFragment<CategoryDetailsView, CategoryDetailsPresenter> implements CategoryDetailsView {
    public static final String ARG_CATEGORY_PARAM = "_argCategoryId";

    @Bind(app.peraza.R.id.toolbar)
    Toolbar toolbar;

    @Bind(app.peraza.R.id.iconsGrid)
    RecyclerView iconsGrid;

    @Bind(app.peraza.R.id.colorPicker)
    LineColorPicker colorPicker;

    @Bind(app.peraza.R.id.categoryName)
    EditText categoryName;

    @Bind(app.peraza.R.id.categoryInputLayout)
    TextInputLayout categoryInputLayout;

    @Inject
    CategoryDetailsPresenter presenter;

    private IconsGridAdapter adapter;
    private Category category;

    @Override
    public CategoryDetailsPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().getUserComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(app.peraza.R.layout.fragment_category_details, null, false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(app.peraza.R.menu.menu_category_details, menu);
        MenuItem saveItem = menu.findItem(app.peraza.R.id.action_save);
        saveItem.setIcon(getResourceHelper().getMaterialToolbarIcon(app.peraza.R.string.ic_material_done));
        if (category != null && category.getId() == null) {
            MenuItem deleteItem = menu.findItem(app.peraza.R.id.action_delete);
            deleteItem.setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case app.peraza.R.id.action_save:
                Category category = getCategory();
                if (validateCategory(category)) {
                    if (TextUtils.isEmpty(category.getId())) {
                        analytics().trackCategoryCreated(category);
                    } else {
                        analytics().trackCategoryUpdated(category);
                    }
                    presenter.updateCategory(getCategory());
                }
                break;
            case app.peraza.R.id.action_delete:
                category = getCategory();
                analytics().trackCategoryDeleted(category);
                presenter.deleteCategory(category);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(toolbar);
        setDisplayHomeAsUpEnabled(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        iconsGrid.setLayoutManager(gridLayoutManager);
        adapter = new IconsGridAdapter(IconUtils.getAll(), getOutlayTheme());
        iconsGrid.setAdapter(adapter);
        colorPicker.setOnColorChangedListener(i -> adapter.setActiveColor(i));

        if (getArguments().containsKey(ARG_CATEGORY_PARAM)) {
            String categoryId = getArguments().getString(ARG_CATEGORY_PARAM);
            getActivity().setTitle(getString(app.peraza.R.string.caption_edit_category));
            presenter.getCategory(categoryId);
        } else {
            getActivity().setTitle(getString(app.peraza.R.string.caption_edit_category));
            showCategory(new Category());
        }

        categoryName.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                categoryInputLayout.setErrorEnabled(false);
            }
        });
    }

    @Override
    public void showCategory(Category category) {
        this.category = category;

        if (category.getId() == null) {
            int colorsCount = colorPicker.getColors().length;
            int randomPosition = new Random().nextInt(colorsCount);
            colorPicker.setSelectedColorPosition(randomPosition);
            adapter.setActiveColor(colorPicker.getColor());
        } else {
            colorPicker.setSelectedColor(category.getColor());
            adapter.setActiveItem(category.getIcon(), category.getColor());
            categoryName.setText(category.getTitle());
        }
    }

    @Override
    public void finish() {
        getActivity().onBackPressed();
    }

    public Category getCategory() {
        category.setTitle(categoryName.getText().toString());
        category.setColor(colorPicker.getColor());
        category.setIcon(adapter.getSelectedIcon());
        return category;
    }

    private boolean validateCategory(Category category) {
        boolean result = true;
        if (TextUtils.isEmpty(category.getTitle())) {
            categoryInputLayout.setErrorEnabled(true);
            categoryInputLayout.setError(getString(app.peraza.R.string.error_category_name_empty));
            categoryName.requestFocus();
            result = false;
        } else if (TextUtils.isEmpty(category.getIcon())) {
            Alert.error(getBaseActivity().getRootView(), getString(app.peraza.R.string.error_category_icon));
            result = false;
        }

        return result;
    }
}