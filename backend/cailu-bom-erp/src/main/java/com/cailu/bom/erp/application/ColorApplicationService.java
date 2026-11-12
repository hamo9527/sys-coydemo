package com.cailu.bom.erp.application;
import java.util.List;
public interface ColorApplicationService { List<ColorView> list(String keyword); ColorView create(ColorCommand command); ColorView update(Long id,ColorCommand command); void delete(Long id); }