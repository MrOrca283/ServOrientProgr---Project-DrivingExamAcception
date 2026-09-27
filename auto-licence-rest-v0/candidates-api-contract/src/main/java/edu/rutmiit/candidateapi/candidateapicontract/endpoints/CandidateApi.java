package edu.rutmiit.candidateapi.candidateapicontract.endpoints;

import edu.rutmiit.candidateapi.candidateapicontract.config.ExamCandidatesApiContractConfig;
import edu.rutmiit.candidateapi.candidateapicontract.dto.*;
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
import org.springframework.web.bind.annotation.*;


@Tag(name = "Candidates", description = "Управление кандадатами в водители")
@RequestMapping(
        value = "/api/candidates",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public interface CandidateApi {

    @Operation(
            summary = "Список кандидатов",
            description = "Возвращает постраничный список кандидатов с HATEOAS-ссылками. "
                    + "Ссылки prev/next позволяют клиенту навигировать по страницам без знания офсетов.",
            security = @SecurityRequirement(name = ExamCandidatesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список кандидатов")
    @GetMapping
    PagedModel<EntityModel<CandidateResponse>> getAllCandidates(
            @Parameter(description = "Номер страницы (0..N)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20")
            @RequestParam(defaultValue = "20") int size
    );

    @Operation(
            summary = "Получить кандидата по ID",
            security = @SecurityRequirement(name = ExamCandidatesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Кандидат найден")
    @ApiResponse(responseCode = "404", description = "Кандидат не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    EntityModel<CandidateResponse> getCandidateById(
            @Parameter(description = "ID кандидата", required = true, example = "1") @PathVariable Long id
    );

    @Operation(
            summary = "Создать кандидата",
            security = @SecurityRequirement(name = ExamCandidatesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Кандидат создан. Location header содержит URI нового ресурса.")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<CandidateResponse>> createCandidate(@Valid @RequestBody CandidateRequest request);

    @Operation(
            summary = "Полное обновление кандидата (PUT)",
            description = "Заменяет все поля кандидата. Для обновления отдельных полей используйте PATCH.",
            security = @SecurityRequirement(name = ExamCandidatesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Кандидат обновлён")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Кандидат не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<CandidateResponse> updateCandidate(
            @Parameter(description = "ID кандидата", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody CandidateRequest request
    );

    @Operation(
            summary = "Частичное обновление кандидата (PATCH)",
            description = """
                    Обновляет только переданные поля (семантика JSON Merge Patch, RFC 7396).
                    Непереданные поля остаются без изменений.
                    """,
            security = @SecurityRequirement(name = ExamCandidatesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Кандидат обновлён")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Кандидат не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<CandidateResponse> patchCandidate(
            @Parameter(description = "ID кандидата", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody PatchCandidateRequest request
    );

    @Operation(
            summary = "Удалить кандидата",
            description = "Удаляет кандидата и все его мед данные (каскадное удаление).",
            security = @SecurityRequirement(name = ExamCandidatesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Кандидат удалён")
    @ApiResponse(responseCode = "404", description = "Кандидат не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCandidate(
            @Parameter(description = "ID кандидата", required = true, example = "1") @PathVariable Long id
    );

    @Operation(
            summary = "Мед данные кандидата (суб-ресурс)",
            description = """
                    Возвращает постраничный список мед данных указанного кандидата.
                    Это суб-ресурс (концепция REST): /candidates/{id}/medBlanks.
                    Эквивалентен GET /medData?candidateId={id}, но точнее отражает иерархию.
                    """,
            security = @SecurityRequirement(name = ExamCandidatesApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список мед данных кандидата")
    @ApiResponse(responseCode = "404", description = "Кандидат не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}/medBlanks")
    PagedModel<EntityModel<MedBlankResponse>> getMedBlanksByCandidate(
            @Parameter(description = "ID кандидата", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Номер страницы (0..N)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );
}
