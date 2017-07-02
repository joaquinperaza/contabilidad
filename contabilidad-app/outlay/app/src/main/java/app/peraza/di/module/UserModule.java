package app.peraza.di.module;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import app.peraza.data.repository.CategoryRepositoryImpl;
import app.peraza.data.repository.ExpenseRepositoryImpl;
import app.peraza.data.source.CategoryDataSource;
import app.peraza.data.source.ExpenseDataSource;
import app.peraza.di.scope.UserScope;
import app.peraza.domain.model.User;
import app.peraza.domain.repository.CategoryRepository;
import app.peraza.domain.repository.ExpenseRepository;
import app.peraza.firebase.CategoryFirebaseSource;
import app.peraza.firebase.ExpenseFirebaseSource;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bmelnychuk on 10/27/16.
 */

@Module
public class UserModule {
    private User user;

    public UserModule(User user) {
        this.user = user;
    }

    @Provides
    @UserScope
    public User provideCurrentUser(
    ) {
        return user;
    }

    @Provides
    @UserScope
    public DatabaseReference provideDatabseRef(
    ) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(user.getId()).keepSynced(true);
        return database;
    }

    @Provides
    @UserScope
    public CategoryDataSource provideFirebaseCategoryDataSource(
            DatabaseReference databaseReference
    ) {
        return new CategoryFirebaseSource(user, databaseReference);
    }

    @Provides
    @UserScope
    public ExpenseDataSource provideExpenseFirebaseSource(
            DatabaseReference databaseReference,
            CategoryDataSource categoryFirebaseSource
    ) {
        return new ExpenseFirebaseSource(user, databaseReference, categoryFirebaseSource);
    }

    @Provides
    @UserScope
    public CategoryRepository provideCategoryRepository(CategoryDataSource firebaseSource) {
        return new CategoryRepositoryImpl(firebaseSource);
    }

    @Provides
    @UserScope
    public ExpenseRepository provideExpenseRepository(ExpenseDataSource expenseDataSource) {
        return new ExpenseRepositoryImpl(expenseDataSource);
    }
}
