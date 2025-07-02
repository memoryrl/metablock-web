package cetus.bean;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import cetus.support.Reflector;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Pageable {
    private Integer countPage = 10;
    private Integer pageNumber = 1;
    private Integer size = 10;

    public Pageable(Integer size) {
        this.size = size;
    }

    public void setCountPage(Integer countPage) {
        if(countPage != null) {
            this.countPage = countPage;
        }
    }

    public void setPageNumber(Integer pageNumber) {
        if(pageNumber != null) {
            this.pageNumber = pageNumber;
        }
    }

    public void setSize(Integer size) {
        if(size != null) {
            this.size = size;
        }
    }
    public final Map<String, Object> generateMap(Object bean) {
        Map<String, Object> map = Reflector.getGetterMap(bean);
        map.put("startNumber", (this.pageNumber - 1) * this.size);
        map.put("endNumber", this.size);
        return map;
    }
}
