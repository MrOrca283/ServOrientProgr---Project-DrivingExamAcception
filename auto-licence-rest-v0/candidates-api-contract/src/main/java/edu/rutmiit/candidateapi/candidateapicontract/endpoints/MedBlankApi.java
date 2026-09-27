package edu.rutmiit.candidateapi.candidateapicontract.endpoints;

import edu.rutmiit.candidateapi.candidateapicontract.dto.MedBlankRequest;
import edu.rutmiit.candidateapi.candidateapicontract.dto.MedBlankResponse;
import edu.rutmiit.candidateapi.candidateapicontract.dto.PatchMedBlankRequest;
import edu.rutmiit.candidateapi.candidateapicontract.dto.UpdateMedBlankRequest;
import edu.rutmiit.candidateapi.candidateapicontract.config.ExamCandidatesApiContractConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;


@Tag(name = "MedBlank", description = "Управление мед данными в базе")
@RequestMapping(
        value = "/api/medicalData",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public interface MedBlankApi {

    @Operation(
            summary = "Получить медбланк по blankCode",
            security = @SecurityRequirement(name = ExamCandidatesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Медбланк найден")
    @ApiResponse(responseCode = "404", description = "Медбланк не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    EntityModel<MedBlankResponse> getMedBlankById(
            @Parameter(description = "blankCode мед карты", required = true, example = "1") @PathVariable Long id
    );

    @Operation(
            summary = "Список медбланков",
            description = """
                    Возвращает постраничный список мед карт с HATEOAS-ссылками.
                    Поддерживает комбинирование фильтров: id, lastName, firstName и titleSearch
                    можно передавать одновременно.
                    """,
            security = @SecurityRequirement(name = ExamCandidatesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Постраничный список мед карт")
    @GetMapping
    PagedModel<EntityModel<MedBlankResponse>> getAllMedBlanks(
            @Parameter(description = "Фильтр по ID кандидата") @RequestParam(required = false) Long candidateId,
            @Parameter(description = "Фильтр по omsId", example = "748590584639") @RequestParam(required = false) String omsId,
            @Parameter(description = "Поиск по blankCode", example = "1") @RequestParam(required = false) String blankCodeSearch,
            @Parameter(description = "Номер страницы (0..N)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );




    @Operation(
            summary = "Создать медбланк",
            security = @SecurityRequirement(name = ExamCandidatesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Мед карта создана. Location header содержит URI нового ресурса.")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Кандидат с указанным id не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Медбланк с таким blankCode уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<MedBlankResponse>> createMedBlank(@Valid @RequestBody MedBlankRequest request);

    @Operation(
            summary = "Полное обновление медбланки (PUT)",
            description = "Заменяет все поля медбланков кроме ФИО, ФИО владельца изменить нельзя. "
                    + "Для обновления отдельных полей используйте PATCH.",
            security = @SecurityRequirement(name = ExamCandidatesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Медбланк обновлена")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "медбланк не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "медбланк с таким BlankCode уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<MedBlankResponse> updateMedBlank(
            @Parameter(description = "blankCode медбланка", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateMedBlankRequest request
    );

    @Operation(
            summary = "Частичное обновление медбланка (PATCH)",
            description = """
                    Обновляет только переданные поля (семантика JSON Merge Patch, RFC 7396).
                    Непереданные поля остаются без изменений. Владельца медбланка изменить нельзя.
                    """,
            security = @SecurityRequirement(name = ExamCandidatesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Медбланк обновлён")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Медбланк не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Медбланк с таким BlankCode уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<MedBlankResponse> patchMedBlank(
            @Parameter(description = "blankCode медбланка", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody PatchMedBlankRequest request
    );

    @Operation(
            summary = "Удалить мед карту",
            security = @SecurityRequirement(name = ExamCandidatesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Медбланк удален")
    @ApiResponse(responseCode = "404", description = "Медбланк не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMedBlank(
            @Parameter(description = "blankCode медбланка", required = true, example = "1") @PathVariable Long id
    );
}

  