package org.springframework.samples.petclinic.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Generic wrapper DTO for statistics responses.
 *
 * @param <T> The type of data in the response
 */
@Schema(description = "Statistics response wrapper")
public class StatisticsResponseDTO<T> {

    @Schema(description = "List of statistics data")
    private List<T> data;

    @Schema(description = "Total count of items", example = "5")
    private int count;

    public StatisticsResponseDTO() {
    }

    public StatisticsResponseDTO(List<T> data) {
        this.data = data;
        this.count = data != null ? data.size() : 0;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
        this.count = data != null ? data.size() : 0;
    }

    public int getCount() {
        return count;
    }
}
