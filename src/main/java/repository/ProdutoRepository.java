package repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import model.Produto;

@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<Produto> {
}
