package ramzy.task.deps;

import javax.inject.Singleton;

import dagger.Component;
import ramzy.task.fragments.BaseServiceFragment;

@Singleton
@Component(modules = {TMBDModule.class})
public interface TMBDComponent {
    void inject(BaseServiceFragment target);
}