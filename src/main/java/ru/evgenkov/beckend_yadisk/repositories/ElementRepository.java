package ru.evgenkov.beckend_yadisk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.evgenkov.beckend_yadisk.model.Element;

public interface ElementRepository extends JpaRepository <Element,String>{
}
