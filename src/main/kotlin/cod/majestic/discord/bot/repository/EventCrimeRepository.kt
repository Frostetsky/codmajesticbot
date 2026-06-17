package cod.majestic.discord.bot.repository

import cod.majestic.discord.bot.model.EventCrime
import cod.majestic.discord.bot.model.projection.CrimeStatisticProjection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface EventCrimeRepository : JpaRepository<EventCrime, UUID> {

    /*@Query(
        """
    SELECT family_member_id,
           COUNT(event_crime_type) AS count_rp,
           MAX(action_time) AS last_action_time,
           event_crime_server,
           points
    FROM cod_majestic_rp_bot.event_crime
    INNER JOIN cod_majestic_rp_bot.family_member fm ON event_crime.family_member_id = fm.id
    WHERE action_time >= :threshold
    GROUP BY family_member_id, event_crime_server, points
    ORDER BY last_action_time
    """
    )*/


}